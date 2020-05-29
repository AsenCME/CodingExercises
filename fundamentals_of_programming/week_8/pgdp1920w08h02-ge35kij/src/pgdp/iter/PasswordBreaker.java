package pgdp.iter;

import java.util.function.Function;

public class PasswordBreaker {

    public static String findPassword(Function<String, Integer> hashFunction, int passwordLength, String salt,
            int saltedPasswordHashValue) {

        if (hashFunction == null)
            Util.badArgument("Hash function not defined!");
        if (salt == null)
            Util.badArgument("Salt function not defined!");

        PasswordIterator iterator = new PasswordIterator(passwordLength);
        while (iterator.hasNext()) {
            String value = iterator.next();
            if (hashFunction.apply(value + salt) == saltedPasswordHashValue)
                return value;
        }

        return null;
    }

}
