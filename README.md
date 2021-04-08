# L4 Individual Project - Remote Touchscreen Control

Publicly shared touchscreens always possessed hygiene issues given that multiple people touch it many times during the day. Recent Covid-19 times have drawn more risks toward susing such shared interfaces. Thus, this project aims to design and implement four remote interation techniques using smartphones. Smartphone can be used a personal remote control as they do not facilitate the transmission of infectious diseases.  

The interaction techniques allow users to remotely control a display cursor using smartphones. 

Technique 1 - Smart Trackpad allows users to relatively move their finger on the trackpad to remotely control the display cursor. Relative and absolute cursor modes are facilitated by this technique. Left and right buttons allow target selection that follow the conventional button functionality. 
![](https://github.com/Sadya-afreen/L4Project-RemoteTouchscreenControl/tree/main/Proj_Images/Trackpad_collage.png)

Technique 2 - Smart Trackball technique allows users to relatively move their finger on the trackball to remotely control the display cursor. This technique required circular finger movements on a smaller area unlike the linear movements on the trackpad. Buttons placed at the bottom of the screen allows selection on the remote screen.

Technique 3 - Smart Tilt Pointer allows users to perform tilt gestures using their smartphone to remotely control a display cursor. Left, right, upwards and downwards tilt will move the cursor left, right, upwards and downwards respectively. Large sized left and right click buttons are placed linearly on the smartphone screen. 

Technique 4 - Smart Rotate Pointer allows users to perform a combination of tilt and rotate gestures using their smartphone to remotely control a display cursor. Clockwise and anticlockwise rotation will move the remote screen cursor towards the right and left respectively whereas upward and downwards tilt will move the cursor upwards and downwards respectively on the screen.

Remote evaluation of the techniques have been performed where technique could be run on an Android Smartphone as an app and for each technique, a server application had to be run on a remote desktop/laptop.

Experiment tasks have been implemented that consists of a variety of 2D and 3D interaction tasks to evaluate the techniques as a 2D and 3D input controller.

This repo containes all the source code of the project (implementation code, evaluation scripts, and executable files), timelog, status report and dissertation report.

Instructions to use the techniques:

On a desktop/laptop (E.g. Windows OS) run a .exe or .jar (E.g. TrackballServer) file from the MyServer folder. {Server-side of the system}
On an Android smartphone, run the app (E.g. trackball) to use the interaction technique to remotely control the display cursor. {Client-side of the system}
Click on the stop connection button displayed on the smartphone screen to disconnect the server.
