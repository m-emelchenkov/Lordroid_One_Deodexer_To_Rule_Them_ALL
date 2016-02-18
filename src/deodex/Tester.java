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

import deodex.controlers.CommandLineWorker;
import deodex.controlers.MainWorker;
import deodex.tools.CmdLogger;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.ui.LangFrame;
import deodex.ui.Window;

public class Tester {
	public static CmdLogger logger = new CmdLogger();
	public static CommandLineWorker rootWorker = new CommandLineWorker();

	public static void main(String args[]) {
		if (args == null || args.length == 0) {
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
				Logger.logToStdIO("[test]" + Cfg.getCurrentLang());
				@SuppressWarnings("unused")
				Window win = new Window();
			}

		} else if (args.length > 3) {
			printHelp();
		} else if (args.length == 1 && args[0].equals("-h")) {
			R.initResources();
			printHelp();
		}else {
			Logger.logToStd = false;
			int lengh = args.length;
			// the user used one argument and it's not '-h' so we assume he
			// selected a folder
			R.initResources();
			File systemFolder = new File(args[0]);
			boolean sign = false;
			boolean zipAlign = false;
			if( lengh == 1){
				sign = false;
				zipAlign = false;
			}else if (lengh == 2) {
				if (args[1].equals("-z") || args[1].equals("-s")) {
					if (args[1].equals("-z")) {
						zipAlign = true;
					}
					if (args[1].equals("-s")) {
						sign = true;
					}
				} else {
					System.out.println("unkown option !");
					printHelp();
					return;
				}
			} else {
				if (args[1].equals("-z") || args[1].equals("-s")) {

					if (args[1].equals("-z")) {
						zipAlign = true;
					}
					if (args[1].equals("-s")) {
						sign = true;
					}
				} else {
					System.out.println("unkown option !");
					printHelp();
					return;
				}
				if (args[2].equals("-z") || args[2].equals("-s")) {

					if (args[2].equals("-z")) {
						zipAlign = true;
					}
					if (args[2].equals("-s")) {
						sign = true;
					}
				} else {
					System.out.println("unkown option !");
					printHelp();
					return;
				}
			}
			if (!systemFolder.exists()) {
				logger.addLog(systemFolder.getAbsolutePath() + " no such folder double check the path !");
				printHelp();
				return;
			} else if (!systemFolder.isDirectory()) {
				logger.addLog(systemFolder.getAbsolutePath() + " is not a directory !");
				printHelp();
				return;
			} else if (FilesUtils.isAValideSystemDir(systemFolder, logger)) {
				proseedWithNoGui(systemFolder, sign, zipAlign);
			}
		}
	}

	private static void proseedWithNoGui(File systemFolder, boolean sign, boolean zipalign) {
		SessionCfg.sign = sign;
		SessionCfg.setSign(sign);
		SessionCfg.zipalign = zipalign;
		SessionCfg.setZipalign(zipalign);
		MainWorker mainWorker = new MainWorker(systemFolder, logger, 1);
		mainWorker.addThreadWatcher(new CommandLineWorker());
		Thread t = new Thread(mainWorker);

		t.start();
	}

	private static void printHelp() {

		System.out.println("Lordroid batch deodex :\n");
		System.out.println("USAGE :\n");
		System.out.println("java -jar lordroid-ODTRTA.jar <systemFolder> [OPTIONS]");
		System.out.println("Options");
		System.out.println("-z : zipalign every apk after deodexing it");
		System.out.println("-s : sign every apk after deodexing");
		System.out.println("-h : print this help page");
	}
}
