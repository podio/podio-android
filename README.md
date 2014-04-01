android-sdk
===========

The Podio Android SDK source code and test suit.

## TEST SUITE ##
To run the test suite locally on your machine you need to have the latest Android SDK available and the $ANDROID_HOME environment variable configured properly.

1. Fetch the latest code from this repository.
1. Make sure you have an emulator running or a physical device connected to your computer.
1. From a terminal, find your way to the test directory of this repository and issue the `ant clean emma debug install test` command.
1. The test suite will be run on the emulator or device of yours and the result will be shown in the terminal window. Also note the "emma" directive which will generate code coverage reports for you. The report is found in the `[PATH_TO_SDK]/test/bin` folder as `coverage.html`.