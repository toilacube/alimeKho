`docker run -d -p 6080:6080 -e EMULATOR_DEVICE="Nexus 7" -e WEB_VNC=true --device /dev/kvm --name android-container budtmo/docker-android:emulator_11.0`

# Setup the enviroment for Windows

## 1. Install Android Studio 
1. For Windows users, go to [Android Studio Download page](https://developer.android.com/studio) 

2. Extract the .exe file and install all the requirements:
  - Android SDK
  - Virutal device
3. Clone the project 
```
git clone https://github.com/toilacube/alimeKho.git
```
4. Open Android Studio, From the Android Studio Welcome screen, select **Open** and choose the project folder

## 2. Run the project on the emulator
### 1. Create virtual devices

1. After opening a project, select **View > Tool Windows > Device Manager** from the main menu bar, and then click **Create device.**

<img src="https://github.com/toilacube/alimeKho/assets/95525386/c2cf9e66-2a70-4a2c-8e07-18e1867c7bf0" width="800" height="600">

2. In the **Select Hardware window**, find Goole Naxus 9 in the **Tablet** and select **Next** 

<img src="https://github.com/toilacube/alimeKho/assets/95525386/bac84c05-24dc-46b0-aa6e-7a9b0a71ca69" width="800" height="600">

3. In the **System image** window, choose to download the Release name **R** (Android 11.0) 

<img src="https://github.com/toilacube/alimeKho/assets/95525386/e42dcb00-afeb-4c47-9e81-deeb25653f66" width="800" height="600">

4. Wait for **R** image being downloaded, then choose **Finish > Next > Finish**

5. Select the device you have downloaded 

<img src="https://github.com/toilacube/alimeKho/assets/95525386/418bf51c-4baa-4344-95c0-437efc8fbeea" width="800" height="600">

6. Click **Run** ![image](https://github.com/toilacube/alimeKho/assets/95525386/dc3e8580-6866-48bd-a744-1d12ec564974) 

7. Username and password for Login screen:

  Username: `nguyenvan1`
  
  Password: `123456`





