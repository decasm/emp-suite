/*
 * @(#)LogFormatter.java	1.14 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package org.oclc.gateman.logging;

import java.io.*;
import java.text.*;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Print a brief summary of the LogRecord in a human readable format on a single line.
 * Same format as java.util.logging.SimpleFormatter, but all on one line.
 */

public class LogFormatter extends Formatter {

	Date dat = new Date();
	private final static String format = "{0,date} {0,time}";
	private MessageFormat formatter;

	private Object args[] = new Object[1];

	// Line separator string.
	private String lineSeparator = System.getProperty("line.separator");

	/**
	 * Format the given LogRecord.
	 * @param record the log record to be formatted.
	 * @return a formatted log record
	 */
	public synchronized String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();
		// Minimize memory allocations here.
		dat.setTime(record.getMillis());
		args[0] = dat;
		StringBuffer text = new StringBuffer();
		if (formatter == null) {
			formatter = new MessageFormat(format);
		}
		formatter.format(args, text, null);
		sb.append(text);
		sb.append(" ");
		if (record.getSourceClassName() != null) {	
			sb.append(record.getSourceClassName());
		}
		else {
			sb.append(record.getLoggerName());
		}
		if (record.getSourceMethodName() != null) {	
			sb.append(" ");
			sb.append(record.getSourceMethodName());
		}
		sb.append(" ");
		String message = formatMessage(record);
		sb.append(record.getLevel().getLocalizedName());
		sb.append(": ");
		sb.append(message);
		sb.append(lineSeparator);
		if (record.getThrown() != null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				sb.append(sw.toString());
			}
			catch (Exception ex) { }
		}
		return sb.toString();
	}
}
