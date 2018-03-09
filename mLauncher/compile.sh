PATH="/opt/android/android-sdk:$PATH"
PATH="/opt/android/android-sdk/tools:$PATH"
PATH="/opt/android/android-sdk/platform-tools:$PATH"
PATH="/opt/android/android-sdk/ndk-bundle:$PATH"
rm -r ./gen ./obj ./bin
./gradlew build
