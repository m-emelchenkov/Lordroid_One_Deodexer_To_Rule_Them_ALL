![logo](https://github.com/lord-ralf-adolf/Lordroid_Universal_Batch_deodexer/blob/master/res/images/logo.png?raw=true)
# Lordroid_Universal_Batch_deodexer
Multi platform Tools to deodex any android rom

### description :  
Lordroid batch deodexer **ODTRTA** One Deodexer To Rule Them All ,is a free software writen in JAVA that can deodex any android rom with ease ,all you have to do is point the system folder and click the Deodex Now button ! it runs under any OS that have JAVA JRE 8 and higher (see prerequired for more details about this limitation) ,it is compatible with all Roms SDK levels and know architectures but you should note that it support list is the same as [smali/backsmali](https://github.com/JesusFreke/smali) && [oat2dex(smaliEX)](https://github.com/testwhat/SmaliEx) support list all roms not compatible with them will not be supported by this software since it uses those softwares to do the job.

### Prerequired  
1. JAVA JRE 8 and higher if you are deodexing sdk>20 Roms.
2. JAVA JRE 6 or higher if you are deodexing sdk<20 Roms.
3. JAVA PATH needs to be set folow those links for more informations[Windows](https://www.java.com/en/download/help/path.xml)| [Linux](http://ask.xmodulo.com/change-default-java-version-linux.html)| [Mac](https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jre.html)

### Download & How to use :  
1. Download this archive [lordroid_ODTRTA_v0.5_beta-1_ALL.tar.gz](https://github.com/lord-ralf-adolf/Lordroid_Universal_Batch_deodexer/releases/download/v0.5-beta1-release/lordroid_ODTRTA_v0.5_beta-1_ALL.tar.gz)
2. Under linux/mac extract the archive with the command `tar xzvf lordroid_ODTRTA_v0.5_beta-1_ALL.tar.gz`
3. Under windows open the archive with 7zip GUI and extract it.
4. If the default file association of `.jar` files is not `JAVA SE` right click then open with `JAVA SE`
5. Click the browse button and choose the ROM's `system` folder or drag and drop a folder to the `Text Field`
6. The log will tell you weither or not you selected folder is valide and if so the deodex Button will be activeted click on it
7. The deodexing can take a lot of time so be patient 
8. when done a log file will be saved under `logs/yyyy-MM-dd_hh-mm-ss.log` check it out to see what happend or just scroll in the log Pannel to see the logs
9. You are done ! the rom is deodexed 
 
  
  
  
### Release notes 
#### V0.5-beta1
1. Please keep in mind this is a beta even though I tested it it might be bugs that I didn't run into.
2. this version supports all rom versions and all architectures.
3. The software detects every thing on it's own no actions are needed from the user for that matter.
4. The software uses two Threads to work no settings is available to change that right now.
5. Pealse reports bugs and suggestions by openning an essue ticket or emailing me at : rachidboudjelida(at)gmail.com
6. have fun guys 
  
  
  
### LICENCE 
#### Code writen by me are under GPL V2 
#### All other libraries see the NOTICE file for more details 
