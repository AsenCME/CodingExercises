package pgdp.net;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class HttpRequest {
	private final HttpMethod method;
	private final String path;
	private final Map<String, String> params;

	public HttpRequest(String firstLine, String body) {
		String[] parts = firstLine.split("\\s+", 3);
		params = new HashMap<>();

		if (parts.length < 3)
			throw new InvalidRequestException("not enough params in request");
		try {
			this.method = HttpMethod.valueOf(parts[0]);
		} catch (Exception e) {
			throw new InvalidRequestException("Unrecognized http method");
		}

		if (parts[1].contains("?")) { // is query
			String[] pathParts = parts[1].split("[?]", 2);
			this.path = pathParts[0];
			if (pathParts.length == 2) {
				var queryParams = pathParts[1].split("[&]");
				for (var param : queryParams)
					addParam(param, "Query params malformed");
			}
		} else
			this.path = parts[1];

		if (this.method == HttpMethod.POST)
			for (var param : body.split("[&]"))
				addParam(param, "Body malformed");
	}

	private void addParam(String param, String errorMessage) {
		var paramParts = param.split("[=]", 2);
		if (paramParts.length != 2)
			throw new InvalidRequestException(errorMessage);
		params.putIfAbsent(paramParts[0], URLDecoder.decode(paramParts[1], StandardCharsets.UTF_8));
	}

	public HttpMethod getMethod() {
		return this.method;
	}

	public String getPath() {
		return this.path;
	}

	public Map<String, String> getParameters() {
		return params;
	}
}
