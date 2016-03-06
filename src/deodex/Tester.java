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
package deodex;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import com.alee.laf.WebLookAndFeel;

import deodex.controlers.CommandLineWorker;
import deodex.controlers.MainWorker;
import deodex.tools.AdbUtils;
import deodex.tools.CmdLogger;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.tools.Os;
import deodex.tools.PathUtils;
import deodex.ui.LangFrame;
import deodex.ui.Window;

public class Tester {
	public static CmdLogger logger = new CmdLogger();
	public static final String[] OPTIONS = { "z", "s", "c" };

	private static void argsReader(String[] args) {
		R.initResources();
		S.initTempFolders();
		boolean zipalign = true;
		boolean sign = false;
		boolean createZip = false;
		boolean adbExtracted = false;
		File systemFolder;
		if (args.length == 2) {
			String source = args[0];
			if (source.equals("e")) {
				adbExtracted = true;
				systemFolder = new File(
						S.EXTRACTED_SYSTEMS.getAbsolutePath() + File.separator + S.getRomExtractionFolderName());
			} else {
				systemFolder = new File(source);
				// does the folder exist ?
				if (!systemFolder.exists()) {
					System.out.println(systemFolder.getAbsolutePath() + " : No such file or directory");
					System.exit(2);
				}
				// can we write in this folder ?
				boolean canWrite = false;
				File writeTest = new File(systemFolder.getAbsolutePath() + File.separator + "test.write");
				try {
					canWrite = writeTest.createNewFile();
					writeTest.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!canWrite) {
					System.out.println(systemFolder.getAbsolutePath() + " : read-only file system "
							+ "\n please make sure that the system folder is read-write before trying again !");
					System.exit(3);
				}

				// obviously if we are here the systemfolder exists and is rw so
				// lets proseed
			}

			String options = args[1];
			for (int i = 0; i < options.length(); i++) {
				String str = "" + options.charAt(i);
				boolean valid = false;
				for (String s : OPTIONS) {
					valid = valid || (s.equals(str));
				}
				if (!valid) {
					System.out.println("Unkown Option  : " + str);
					printHelp();
					return;
				}
			}
			// if we didn't return every thing is ok !
			zipalign = options.contains("z");
			sign = options.contains("s");
			createZip = options.contains("c");
			Tester.proseedWithNoGui(systemFolder, sign, zipalign, createZip, adbExtracted);
		} else {
			String source = args[0];
			systemFolder = new File(source);
			if (!systemFolder.exists()) {
				System.out.println(systemFolder.getAbsolutePath() + " : No such file or directory");
				System.exit(2);
			}
			// can we write in this folder ?
			boolean canWrite = false;
			File writeTest = new File(systemFolder.getAbsolutePath() + File.separator + "test.write");
			try {
				canWrite = writeTest.createNewFile();
				writeTest.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!canWrite) {
				System.out.println(systemFolder.getAbsolutePath() + " : read-only file system "
						+ "\n please make sure that the system folder is read-write before trying again !");
				System.exit(3);
			}
			Tester.proseedWithNoGui(systemFolder, sign, zipalign, createZip, adbExtracted);
		}

	}

	private static void logOsInfo() {
		// lets log SystemInfos
		Logger.logToStdIO("[Tester][I]" + Cfg.getCurrentLang());
		Logger.writLog("[Tester][I]User Os is " + Cfg.getOs());
		Logger.writLog("[Tester][I]Os name : " + Os.getOsName());
		Logger.writLog("[Tester][I]User Platform is : " + Os.platform());
		Logger.writLog("[Tester][I]JAVA version : " + System.getProperty("java.version"));
	}

	public static void main(String args[]) {

		WebLookAndFeel.install();

		if (args == null || args.length == 0) {
			PathUtils.logCallingProcessLocation();
			logOsInfo();
			HostInfo.logInfo();
			if (Cfg.isFirstLaunch()) {
				Cfg.setCurrentLang(S.ENGLISH);
				R.initResources();
				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						@SuppressWarnings("unused")
						LangFrame win = new LangFrame();
					}
				});

			} else {
				Cfg.readCfg();
				R.initResources();
				S.initTempFolders();
				@SuppressWarnings("unused")
				Window win = new Window();
			}

		} else if (args.length > 2) {
			Logger.logToStd = false;
			printHelp();
		} else if (args.length == 1 && args[0].equals("h")) {
			Logger.logToStd = false;
			R.initResources();
			printHelp();
		} else {
			Logger.logToStd = false;
			argsReader(args);
		}
	}

	private static void printHelp() {
		System.out.println("_____________________________________________________________");
		System.out.println("|         Lordroid One Deodexer To Rule'em All v1.20        |");
		System.out.println("|-----------------------------------------------------------|");
		System.out.println("|                                                           |");
		System.out.println("| USAGE :                                                   |");
		System.out.println("| java -jar Launcher.jar <source> [OPTIONS]                 |");
		System.out.println("|-----------------------------------------------------------|");
		System.out.println("| <source> can be either                                    |");
		System.out.println("|-----------------------------------------------------------|");
		System.out.println("| PATH to System Folder exemple : /path/system              |");
		System.out.println("|                   OR                                      |");
		System.out.println("| e : to extract systemFolder directlly from device         |");
		System.out.println("|-----------------------------------------------------------|");
		System.out.println("|                                                           |");
		System.out.println("| Options :                                                 |");
		System.out.println("|-----------------------------------------------------------|");
		System.out.println("| c : create a flashabe zip  after deodexing the rom        |");
		System.out.println("| z : zipalign every apk after deodexing it                 |");
		System.out.println("| s : sign every apk after deodexing                        |");
		System.out.println("| h : print this help page                                  |");
		System.out.println("| please note that options should'nt be separated by spaces |");
		System.out.println("|                                                           |");
		System.out.println("|-----------------------------------------------------------|");
		System.out.println("| Exemple :                                                 |");
		System.out.println("|-----------------------------------------------------------|");
		System.out.println("| java -jar Launcher.jar /path/system zsc                   |");
		System.out.println("| this command will deodex   and sign and zipalign          |\n"
				+ "| and then creates a flashable zip file                     |");
		System.out.println("| java -jar Launcher.jar e  zsc                             |");
		System.out.println("| this command will extract and deodex                      |\n"
				+ "| from connected device                                     |\n"
				+ "| then sign and zipalign                                    |\n"
				+ "| and then creates a flashable zip file                     |");
		System.out.println("|                                                           |\n"
				+ "|-----------------------------------------------------------|\n"
				+ "| NOTE :                                                    |");
		System.out.println("|-----------------------------------------------------------|");
		System.out.println("|extracted systems will be under extracted_system_folders   |");
		System.out.println("|create flashable zip will be under flashable_zips_out      |");
		System.out.println("|-----------------------------------------------------------|");
		System.out.println("|                 © Rachid Boudjelida 2016                  |");
		System.out.println("|             Software distributed under GPL V3             |");
		System.out.println("|___________________________________________________________|");

	}

	private static void proseedWithNoGui(File systemFolder, boolean sign, boolean zipalign, boolean createZip,
			boolean fromdevice) {
		// lets check if system folder is a valid one
		boolean valid = FilesUtils.isAValideSystemDir(systemFolder, logger);
		if (!valid) {
			System.exit(3);
		}
		if (fromdevice) {
			AdbUtils.extractSystem(systemFolder, logger);
		}
		SessionCfg.setSign(sign);
		SessionCfg.setZipalign(zipalign);
		MainWorker mainWorker = new MainWorker(systemFolder, logger, 1);
		mainWorker.addThreadWatcher(new CommandLineWorker(createZip));
		Thread t = new Thread(mainWorker);
		t.start();
	}

}
