#!/bin/sh
rm -rf .idea
./gradlew clean
rm -rf .gradle
rm -rf build
rm -rf */build
rm -rf staffIOSApp/staffIOSApp.xcworkspace
rm -rf staffIOSApp/Pods
rm -rf staffIOSApp/staffIOSApp.xcodeproj/project.xcworkspace
rm -rf staffIOSApp/staffIOSApp.xcodeproj/xcuserdata
rm -rf dealerIOSApp/dealerIOSApp.xcworkspace
rm -rf dealerIOSApp/Pods
rm -rf dealerIOSApp/dealerIOSApp.xcodeproj/project.xcworkspace
rm -rf dealerIOSApp/dealerIOSApp.xcodeproj/xcuserdata
rm -rf cdoIOSApp/cdoIOSApp.xcworkspace
rm -rf cdoIOSApp/Pods
rm -rf cdoIOSApp/cdoIOSApp.xcodeproj/project.xcworkspace
rm -rf cdoIOSApp/cdoIOSApp.xcodeproj/xcuserdata
