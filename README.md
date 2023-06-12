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
4. Open Android Studio, select Import existing project and choose the project folder

## 2. Run the project on the emulator
### 1. Create virtual devices

1. After opening a project, select **View > Tool Windows > Device Manager** from the main menu bar, and then click **Create device.**

<img src="https://github.com/toilacube/alimeKho/assets/95525386/c2cf9e66-2a70-4a2c-8e07-18e1867c7bf0" width="800" height="600">

2. In the **Select Hardware window**, find Goole Naxus 9 in the **Tablet** and select **Next** 
![image](https://github.com/toilacube/alimeKho/assets/95525386/bac84c05-24dc-46b0-aa6e-7a9b0a71ca69)

3. In the **System image** window, choose to download the Release name **R** (Android 11.0) 
![image](https://github.com/toilacube/alimeKho/assets/95525386/46d90c08-80c7-450e-9e63-b7833ae05aab)

4. Wait for **R** image beign downloaded, then choose **Next > Finish**

5. Select the device has just been downloaded 

![image](https://github.com/toilacube/alimeKho/assets/95525386/f6326add-eb62-4a6f-8342-18d52340d9af)

Click **Run** ![image](https://github.com/toilacube/alimeKho/assets/95525386/dc3e8580-6866-48bd-a744-1d12ec564974) 






