#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_app_wffr_api_AccessApi_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "as34fsf@fnm";
    return env->NewStringUTF(hello.c_str());
}