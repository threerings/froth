//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamApps.h"

class DlcInstalledCallback
{
public:

    DlcInstalledCallback (JNIEnv* env) :
        _env(env), _responseCallback(this, &DlcInstalledCallback::dlcInstalled)
    {}

protected:

    STEAM_CALLBACK(DlcInstalledCallback, dlcInstalled,
            DlcInstalled_t, _responseCallback) {
        jclass clazz = _env->FindClass("com/threerings/froth/SteamApps");
        jmethodID mid = _env->GetStaticMethodID(clazz, "dlcInstalledResponse", "(I)V");
        _env->CallStaticVoidMethod(clazz, mid, (jint)pParam->m_nAppID);
    }

    /** The Java environment pointer. */
    JNIEnv* _env;
};

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamApps_getCurrentGameLanguage (
    JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(SteamApps()->GetCurrentGameLanguage());
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamApps_isDlcInstalled (
    JNIEnv* env, jclass clazz, jint appId)
{
    return SteamApps()->BIsDlcInstalled(appId);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamApps_addNativeDlcInstalledCallback (
        JNIEnv* env, jclass clazz)
{
    new DlcInstalledCallback(env);
}
