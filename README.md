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
1. Download the [Latest version](https://goo.gl/H2WrfZ)
 

### How to use (GUI) :
1. Under `linux/mac` extract the archive with the command `tar xzvf lordroid_ODTRTA_v0.5_beta-1_ALL.tar.gz`
2. Under `windows` open the archive with `7zip` GUI and extract it.
3. If the default file association of `.jar` files is not `JAVA SE` right click then open with `JAVA SE`
4. Click the browse button and choose the ROM's `system` folder or drag and drop a folder to the `Text Field`
5. The log will tell you weither or not you selected folder is valide and if so the deodex Button will be activeted click on it
6. The deodexing can take a lot of time so be patient 
7. when done a log file will be saved under `logs/yyyy-MM-dd_hh-mm-ss.log` check it out to see what happend or just scroll in the log Pannel to see the logs
8. You are done ! the rom is deodexed 
  
  
### How to use (Command line) :
         __Lordroid One Deodexer To Rule'em All__              |
         ------------------------------------------        |
 __USAGE :__                                                   |
 `java -jar Launcher.jar <source> [OPTIONS]`               |
 __`<source>` can be either__                                  |
 PATH to System Folder exemple : `/path/system`              |
                   OR                                      |
 `e` : to extract systemFolder directlly from device         |
 __Options :__                                                 |
 `c` : create a flashabe zip  after deodexing the rom        |
 `z` : zipalign every apk after deodexing it                 |
 `s` : sign every apk after deodexing                        |
 `h` : print this help page                                  |
 please note that options should'nt be separated by spaces |
 __Exemple :__                                                 |
 `java -jar Launcher.jar /path/system zsc`                  |
 this command will deodex   and sign and zipalign and then creates a flashable zip file         |
 `java -jar Launcher.jar e  zsc`                             |
 this command will extract and deodex from connected device then sign and zipalign and then creates a flashable zip file|
 __NOTE :__                                                  |
extracted systems will be under `extracted_system_folders`   |
create flashable zip will be under `flashable_zips_out`      |

  

#### Guide lines & troubleshooting :
1. make sure the is a build.prop file in the selected folder 
2. make sure the is a framwork folder under the selected folder.
3. make sure the rom is odexed ! make a search in system folder with the request ".odex" if no results are there then your rom is deodexed and you don't need to run this tool on it.
4. make sure `JRE 7` or higher is the default JRE see the output of java -version you should see `1.8.x` or `1.7.x` if `1.6.x` or lower the tool will not work for roms > sdk 20 lollipop and above !
5. make sur the system folder you chose is writable (means you have the right to write inside it) otherwise the tool will not work.
6. if your rom is sdk > 20 make sure you have a `boot.oat` file under `framework/**` other wise the tool will not work.
7. if you followed all those instructions and you are still having trouble send me a link to your rom or list me the all files under `/system/framework` ,`/system/app` and `/system/priv-app`.
  
  
### Build 
#### using `ant 1.7` or higher
     mkdir deodexer
     git clone https://github.com/lord-ralf-adolf/Lordroid_One_Deodexer_To_Rule_Them_ALL.git deodexer
     cd deodexer  

  
to build a for testing type the command   
```
ant
```
  
to create a release .tar.gz archive type the command the tar.gz will be under re-dist folder   
```
ant release
```
  
to clean the repo   
  
  ```
ant clean  
```
  
  
### LICENCE 
#### The software is licensed under GPL V3 feel free to do what ever you like with it of course in complience with the GPL V3 license :smile: 
#### All (TM) noted in any location of the software are there for reference purpuse only none of them are associated with this project  
