package pgdp.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PinguPlagWebServer {

	static int port = 80;
	private final PinguTextCollection collection = new PinguTextCollection();
	private final HtmlGenerator generator = new HtmlGenerator();
	private final ServerSocket server;

	public PinguPlagWebServer() throws IOException {
		server = new ServerSocket(port);
	}

	public static void main(String[] args) throws IOException {
		PinguPlagWebServer pinguPlagWebServer = new PinguPlagWebServer();
		pinguPlagWebServer.run();
	}

	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				var client = server.accept();
				handleNewClient(client);
			} catch (Exception e) {
			}
		}
	}

	void handleNewClient(Socket client) throws IOException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try (var in = new BufferedReader(
						new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
						var out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8)) {
					String firstLine = in.readLine();
					if (firstLine == null)
						throw new InterruptedException("Could not get reqest");
					String body = tryReadBody(in);
					var res = handleRequest(firstLine, body);
					out.println(res.toString());
					client.close();
				} catch (Exception e) {
					System.err.println("Could not establish connection to client");
				}
			}
		}).start();
	}

	HttpResponse handleRequest(String firstLine, String body) {
		var req = new HttpRequest(firstLine, body);
		if (req.getPath().equals("/"))
			return handleStartPage(req);
		else if (req.getPath().equals("/texts"))
			return handleNewText(req);
		else if (req.getPath().startsWith("/texts/"))
			return handleTextDetails(req);
		return new HttpResponse(HttpStatus.NOT_FOUND, "Path not found");
	}

	HttpResponse handleStartPage(HttpRequest request) {
		if (request.getMethod() != HttpMethod.GET)
			return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed on /");
		String page = this.generator.generateStartPage(this.collection.getAll());
		return new HttpResponse(HttpStatus.OK, page);
	}

	HttpResponse handleTextDetails(HttpRequest request) {
		if (request.getMethod() != HttpMethod.GET)
			return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed on /texts/:id");

		var pathParts = request.getPath().split("[/]", 3);
		if (pathParts.length != 3)
			return new HttpResponse(HttpStatus.BAD_REQUEST, "Id was not provided");
		long id = -1;
		try {
			id = Long.parseLong(pathParts[2]);
		} catch (Exception e) {
			return new HttpResponse(HttpStatus.BAD_REQUEST, "Invalid id");
		}
		var text = collection.findById(id);
		if (text == null)
			return new HttpResponse(HttpStatus.BAD_REQUEST, "ID not found");
		String page = generator.generateTextDetailsPage(text, this.collection.findPlagiarismFor(id));
		return new HttpResponse(HttpStatus.OK, page);
	}

	HttpResponse handleNewText(HttpRequest request) {
		if (request.getMethod() != HttpMethod.POST)
			return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed on /text");
		var params = request.getParameters();
		var title = params.getOrDefault("title", null);
		var author = params.getOrDefault("author", null);
		var text = params.getOrDefault("text", null);
		if (title == null || author == null || text == null)
			return new HttpResponse(HttpStatus.BAD_REQUEST, "Body is missing params");
		var newText = this.collection.add(title, author, text);
		return new HttpResponse(HttpStatus.SEE_OTHER, null, "/texts/" + newText.getId());
	}

	PinguTextCollection getPinguTextCollection() {
		return this.collection;
	}

	/**
	 * Tries to read a HTTP request body from the given {@link BufferedReader}.
	 * Returns null if no body was found. This method consumes all lines of the
	 * request, read the first line of the HTTP request before using this method.
	 */
	static String tryReadBody(BufferedReader br) throws IOException {
		String contentLengthPrefix = "Content-Length: ";
		int contentLength = -1;
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) {
				if (contentLength == -1)
					return null;
				char[] content = new char[contentLength];
				int read = br.read(content);
				if (read == -1)
					return null;
				if (read < content.length)
					content = Arrays.copyOf(content, read);
				return new String(content);
			}
			if (line.startsWith(contentLengthPrefix)) {
				try {
					contentLength = Integer.parseInt(line.substring(contentLengthPrefix.length()));
				} catch (@SuppressWarnings("unused") RuntimeException e) {
					// ignore and just continue
				}
			}
		}
		return null;
	}
}
