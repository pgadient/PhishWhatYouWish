# Phish What You Wish
Browser isolation implemented as an image-based HTTP proxy server.
Full support is available for all major web browsers and partial support for most mobile web browsers.

The Proof-of-Concept relies on Java, Selenium, Firefox, and HTML5.

> This is the accompanying material for the publication titled “Phish What You Wish” that has been presented at the 21st IEEE International Conference on Software Quality, Reliability, and Security (QRS 2021). The preprint can be downloaded [here](https://arxiv.org/pdf/2111.03596) and the presentation is available [here](https://www.youtube.com/watch?v=XEWyGN_P_pw).

## Phishing server
The server application has a fallback to the default configuration when no start-up parameters are provided.
The application is ready for clients after the remote-controlled browser finished opening the specified website after few seconds.

> Important for JAR file execution:
Since the tool creates temporary folders at the execution path for the built-in web server, the compiled JAR file cannot be directly started from within a ZIP file.

The phished website can then be accessed at:
`http://localhost:8080`

### System requirements
- Windows (x86 and x64), MacOS, or most Debian-based Linux OSes (x86 and x64)
- An installed recent version of Mozilla Firefox
- A non-retina display (retina displays may suffer from visual glitches)
- OpenJDK 11 or higher
- Security permissions to access ports on localhost (relevant network firewall dialogs depend on the OS and should pop up automatically)

### Supported features
- Mouse clicks (currently only left button)
- Keyboard presses
- Copy-paste events into text boxes with keyboard short-cuts
- Hover effects for mouse cursor on most hyperlinks and buttons
- Favicons
- Browser history with favicons
- Adapted URL in address bar
- Frames-per-second (fps) indicator on top right that indicates the received screenshots per second
- Support for credential proposals (due to overlayed text boxes)
- Support for Google Captcha's although they occasionally take more time to complete than usual
- Drag support for sliders (currently only start / end point of the drag is submitted, but still allows to bypass most slider protections)
- `F8` key restarts the Selenium back end
- ...

### (Yet) unsupported features
-	Resolutions other than 1920 x 1080 at 100% scaling
-	Pop-up windows 

### Command-line arguments
`java -jar PhishingServer.jar [hideHeadlessBrowser?] [enableAdBlockPlugin?] [provideDebugOutput?] URL_to_phish`

Example:
`java -jar PhishingServer.jar false true true "https://www.google.com/"`

The command above starts a phishing server with a visible headless browser, enabled ad-blocking and debug console output.
NB: This is the default configuration.

## Troubleshooting
If problems occur during start-up, please ensure:
- the requirements are met
- you use the full hd screen resolution (1920x1080, 100% scaling)
- no other process uses the network ports 8080 (HTTP) and 8081 (web socket communication)
- access to these two ports has been granted to the Java executable (OS dialogs should occur automatically)
- no software firewall is blocking these two ports (might happen when 3rd party software firewalls are in use)
- no orphaned processes are running (from Java, Firefox, or geckodriver instances)
- If exported as JAR application: the JAR file has not been executed from within a ZIP file