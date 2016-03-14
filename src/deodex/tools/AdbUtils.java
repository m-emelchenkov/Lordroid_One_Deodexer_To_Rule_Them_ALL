/*
 *  Lordroid One Deodexer To Rule Them All
 * 
 *  Copyright 2016 Rachid Boudjelida <rachidboudjelida@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package deodex.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import deodex.Cfg;
import deodex.R;
import deodex.S;
import deodex.controlers.LoggerPan;

public class AdbUtils {
	/**
	 * the null device to be used when no device was found
	 */
	public static final String NULL_DEVICE = "null|null";

	/**
	 * will extract /system/app ,/system/priv-app ,/system/framework
	 * ,/system/*.sqsh files and build.prop of course system/framwork needs to
	 * be extracted so the only case this returns false is when it can't extract
	 * build.prop or framework folder or on a non zero adb exit code.
	 * 
	 * @param outputFolder
	 *            the folder in which the extracted files will be stored (needs
	 *            to be a directory) the directory will be created make sure you
	 *            have write acess
	 * @param logger
	 *            : the logger who will handle the logs and show them to the
	 *            user do not send null
	 * @return boolean : true only if build.prop and framework were extracted
	 */
	public static boolean extractSystem(File outputFolder, LoggerPan logger) {
		AdbUtils.killServer();
		AdbUtils.startServer();
		int sdk = 999;
		File buildPropOut = new File(outputFolder.getAbsolutePath() + "/build.prop");
		File privAppOut = new File(outputFolder.getAbsolutePath() + "/priv-app");
		File appOut = new File(outputFolder.getAbsolutePath() + "/app");
		File framworkOut = new File(outputFolder.getAbsolutePath() + "/framework");

		logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000029"));
		String[] cmd = { S.getAdbBin(), "pull", "/system/build.prop", buildPropOut.getAbsolutePath() };
		boolean copyprop = CmdUtils.runCommand(cmd) == 0;
		if (!copyprop) {
			logger.addLog(R.getString(S.LOG_ERROR) + R.getString("0000030"));
			return false;
		}
		logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000031"));

		logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000032"));
		try {
			sdk = Integer.parseInt(PropReader.getProp(S.SDK_LEVEL_PROP, buildPropOut));
		} catch (Exception e) {
			logger.addLog(R.getString(S.LOG_ERROR) + R.getString("0000033"));
		}
		logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000034") + sdk);

		if (sdk > 18) {
			logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000035"));
			String[] privAppCmd = { S.getAdbBin(), "pull", "system/priv-app",
					privAppOut.getAbsolutePath() };
			boolean privAppStatus = CmdUtils.runCommand(privAppCmd) == 0;
			if (!privAppStatus) {
				logger.addLog(R.getString(S.LOG_WARNING + R.getString("0000036")));
			}
			logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000037"));

		}
		// copy system app
		logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000038"));
		String[] appCmd = { S.getAdbBin(), "pull", "/system/app", appOut.getAbsolutePath() };
		boolean appStatus = CmdUtils.runCommand(appCmd) == 0;
		if (!appStatus) {
			logger.addLog(R.getString(S.LOG_WARNING + R.getString("0000039")));
		}
		logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000040"));

		// check for squash files
		File appSquashOutput = new File(outputFolder.getAbsolutePath() + File.separator + "odex.app.sqsh");
		File privAppSquashOutput = new File(outputFolder.getAbsolutePath() + File.separator + "odex.priv-app.sqsh");
		String[] appSquashCmd = { S.getAdbBin(), "pull", "/system/odex.app.sqsh",
				appSquashOutput.getAbsolutePath() };
		String[] privAppSquashCmd = { S.getAdbBin(), "pull", "/system/odex.priv-app.sqsh",
				privAppSquashOutput.getAbsolutePath() };
		boolean squash = CmdUtils.runCommand(appSquashCmd) == 0;
		CmdUtils.runCommand(privAppSquashCmd);
		// TODO externalize this

		if (squash)
			logger.addLog(R.getString(S.LOG_INFO)
					+ ".sqsh Files were detected it will be extracted no action needed from user... ");

		// copy framwork
		logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000041"));
		String[] framCmd = { S.getAdbBin(), "pull", "/system/framework", framworkOut.getAbsolutePath() };
		boolean framStatus = CmdUtils.runCommand(framCmd) == 0;
		if (framStatus)
			logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000042"));
		else
			logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000043"));
		return framStatus;
	}

	/**
	 * will return only the device name from the output of "adb devices"
	 * 
	 * @param out
	 *            : must be the output on "adb devices" command
	 * @return deviceName the device name parsed from the out parameter
	 */
	private static String getDeviceName(String out) {
		String tmp = "";
		for (int i = 0; i < out.length(); i++) {
			if (out.charAt(i) != '	') {
				tmp = tmp + out.charAt(i);
			} else {
				break;
			}
		}
		return tmp;
	}

	/**
	 * @param logger
	 *            : a LoggerPan to write the logging outputs (can't be null ! )
	 *            to by pass this if you wanna use a null parameter create a
	 *            blank LoggerPan implementation that does nothing on the
	 *            implemented methods
	 * @return device with status formated like this device|status
	 */
	public static String getDevices(LoggerPan logger) {
		String formatedDevice = "";
		boolean killStatus = killServer();
		if (!killStatus) {
			Logger.writLog("[AdbUtils][E] adb server couldn't be killed aborting ...");
			logger.addLog(R.getString(S.LOG_ERROR) + R.getString("0000019"));
			return NULL_DEVICE;
		}

		boolean startStatus = startServer();
		if (!startStatus) {
			Logger.writLog("[AdbUtils][E] adb server couldn't be started aborting ...");
			logger.addLog(R.getString(S.LOG_ERROR) + R.getString("0000020"));
			return NULL_DEVICE;
		}

		String[] cmd = { S.getAdbBin(), "devices" };

		Runtime rt = Runtime.getRuntime();
		Process p = null;

		try {
			p = rt.exec(cmd);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			// read the output from the command
			ArrayList<String> output = new ArrayList<String>();
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				output.add(s);
			}
			// read any errors from the attempted command
			ArrayList<String> errors = new ArrayList<String>();
			while ((s = stdError.readLine()) != null) {
				errors.add(s);
			}

			int exitValue = p.waitFor();
			if (exitValue != 0) {
				logger.addLog(R.getString(S.LOG_ERROR) + R.getString("0000021"));
				Logger.writLog("[AdbUtils][E]" + "adb exited with no zero code error=" + exitValue);
				return NULL_DEVICE;
			}
			if (output.size() > 3) {
				logger.addLog(R.getString(S.LOG_ERROR + R.getString("0000022")));
				return NULL_DEVICE;
			}
			if (output.size() < 3) {
				logger.addLog(R.getString(S.LOG_ERROR) + R.getString("0000023"));
				if (Cfg.getOs().equals("windows"))
					logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000024"));
				else if (Cfg.getOs().equals("linux"))
					logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000025"));
				else if (Cfg.getOs().equals("osx")) {
					logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000026"));
				}

				return NULL_DEVICE;
			}
			if (output.size() == 3) {
				formatedDevice = getDeviceName(output.get(1)) + "|" + AdbUtils.getDeviceStatus(output.get(1));
				logger.addLog(R.getString(S.LOG_INFO) + R.getString("0000027") + getDeviceName(output.get(1))
						+ R.getString("0000028") + AdbUtils.getDeviceStatus(output.get(1)));
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return NULL_DEVICE;
		}

		return formatedDevice;
	}

	/**
	 * will read the device status from the "adb devices" command output
	 * received as a parameter and return only the status value
	 * 
	 * @param out
	 *            : must be the output of the command adb devices as a
	 *            string @see : AdbUtils.getDevices(LoggerPan logger)
	 * @return status : the Device Status
	 */
	private static String getDeviceStatus(String out) {
		return out.substring(out.lastIndexOf("	") + 1, out.length());
	}

	/**
	 * this will run "adb kill-server" and gets it's output returns true only if
	 * the adb exit code was 0
	 * 
	 * @return serverWasKilled ?
	 */
	private static boolean killServer() {
		// if we have no adb binary dont bother
		if (Cfg.getOs().equals("null")) {
			Logger.writLog("[AdbUtils][E]ADB is not supported by this OS");
			return false;
		}
		String[] cmd = { S.getAdbBin(), "kill-server" };
		return CmdUtils.runCommand(cmd) == 0;
	}

	/**
	 * this will run "adb start-server" and gets it's output returns true only
	 * if the adb exit code was 0
	 * 
	 * @return serverStarted ?
	 */
	private static boolean startServer() {
		// if we don't have adb binaries
		if (Cfg.getOs().equals("null")) {
			Logger.writLog("[AdbUtils][E]ADB is not supported by this OS");
			return false;
		}
		String[] cmd = { S.getAdbBin(), "start-server" };
		return CmdUtils.runCommand(cmd) == 0;
	}

}
