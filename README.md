![logo](https://github.com/lord-ralf-adolf/Lordroid_Universal_Batch_deodexer/blob/master/res/images/logo.png?raw=true)
# Lordroid_Universal_Batch_deodexer
Multi platform Tools to deodex any android rom

### description :  
Lordroid batch deodexer **ODTRTA** One Deodexer To Rule Them All ,is a free software writen in JAVA that can deodex any android rom with ease ,all you have to do is point the system folder and click the Deodex Now button ! it runs under any OS that have JAVA JRE 8 and higher (see prerequired for more details about this limitation) ,it is compatible with all Roms SDK levels and know architectures but you should note that it support list is the same as [smali/backsmali](https://github.com/JesusFreke/smali) && [oat2dex(smaliEX)](https://github.com/testwhat/SmaliEx) support list all roms not compatible with them will not be supported by this software since it uses those softwares to do the job.

### Prerequired  
1. JAVA JRE 8 and higher if you are deodexing sdk>20 Roms.
2. JAVA JRE 6 or higher if you are deodexing sdk<20 Roms.
3. JAVA PATH needs to be set folow those links for more informations[Windows](https://www.java.com/en/download/help/path.xml)| [Linux](http://ask.xmodulo.com/change-default-java-version-linux.html)| [Mac](https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jre.html)

### Download :  
1. Download this archive [lordroid_ODTRTA_v0.5_beta-1_ALL.tar.gz](https://github.com/lord-ralf-adolf/Lordroid_Universal_Batch_deodexer/releases/download/v0.6_beta2-release/lordroid_ODTRTA_v0.6-beta-2_ALL.tar.gz)
 

### How to use (GUI) :
1. Under linux/mac extract the archive with the command `tar xzvf lordroid_ODTRTA_v0.5_beta-1_ALL.tar.gz`
2. Under windows open the archive with 7zip GUI and extract it.
3. If the default file association of `.jar` files is not `JAVA SE` right click then open with `JAVA SE`
4. Click the browse button and choose the ROM's `system` folder or drag and drop a folder to the `Text Field`
5. The log will tell you weither or not you selected folder is valide and if so the deodex Button will be activeted click on it
6. The deodexing can take a lot of time so be patient 
7. when done a log file will be saved under `logs/yyyy-MM-dd_hh-mm-ss.log` check it out to see what happend or just scroll in the log Pannel to see the logs
8. You are done ! the rom is deodexed 
  
  
### How to use (Command line) :
1. Usage  `java -jar lordroid-ODTRTA.jar <System_Folder> [Options]`
2. Options :`-z` to zipalign after deodexing , `-s` to resign apps apfter deodexing is done.
3. `java -jar lordroid-ODTRTA.jar  -h`  to display help.
  
  
  
### Release notes 
#### V0.5-beta2
1. New command line tool `java -jar lordroid-ODTRTA.jar -h` for more details
2. Better detection for already deodexed roms 
3. You can now choose the max threads to be used !
4. Code optimized for less HDD IOs may reduce the deodexing time.
5. better handling of some Exceptions.
6. you can get ride of the alert popup messages after the first apearence.

#### V0.5-beta1
1. Please keep in mind this is a beta even though I tested it it might be bugs that I didn't run into.
2. this version supports all rom versions and all architectures.
3. The software detects every thing on it's own no actions are needed from the user for that matter.
4. The software uses two Threads to work no settings is available to change that right now.
5. Pealse reports bugs and suggestions by openning an essue ticket or emailing me at : rachidboudjelida(at)gmail.com
6. have fun guys 
  
  

#### Guide lines & troubleshooting :
1. make sure the is a build.prop file in the selected folder 
2. make sure the is a framwork folder under the selected folder.
3. make sure the rom is odexed ! make a search in system folder with the request ".odex" if no results are there then your rom is deodexed and you don't need to run this tool on it.
4. make sure JRE 8 is the default JRE see the output of java -version you should see 1.8.x if 1.7.x or lower the tool will not work for roms > sdk 20 lollipop and above !
5. make sur the system folder you chose is writable (means you have the right to write inside it) otherwise the tool will not work.
6. if your rom is sdk > 20 make sure you have a boot.oat file under framework/*arch* other wise the tool will not work.
7. if you followed all those instructions and you are still having trouble send me a link to your rom or list me the all files under /system/framework ,/system/app and /system/priv-app.

  
### LICENCE 
#### Code writen by me are under GPL V2 
#### All other libraries see the NOTICE file for more details 
