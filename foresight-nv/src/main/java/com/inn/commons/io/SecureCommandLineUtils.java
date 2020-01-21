package com.inn.commons.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.owasp.esapi.ExecuteResult;
import org.owasp.esapi.Executor;
import org.owasp.esapi.errors.ExecutorException;
import org.owasp.esapi.reference.DefaultExecutor;

import com.inn.commons.Symbol;
import com.inn.commons.Validate;
import com.inn.commons.lang.NumberUtils;

/**
 * Secure Utility to execute linux commands through Java.
 *
 */

public class SecureCommandLineUtils {

    private SecureCommandLineUtils() {
        super();
    }

    public static ExecuteResult execute(String command) throws ExecutorException {
        Validate.checkNotEmpty(command, "Command not found");
        String[] cmd = StringUtils.split(command, Symbol.SPACE);
        List<String> params = new ArrayList<>();
        Executor instance = DefaultExecutor.getInstance();
        File executable = new File(cmd[NumberUtils.INTEGER_ZERO]);
        for (int i = 1; i < cmd.length; i++) {
            if (StringUtils.isNotEmpty(cmd[i])) {
                params.add(cmd[i]);
            }
        }
        return instance.executeSystemCommand(executable, params);
    }
}