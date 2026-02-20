//
// $Id$

#include <steam/steam_api.h>
#include <steam/steam_gameserver.h>

#include "com_threerings_froth_SteamUtils.h"

static ISteamUtils* getUtils ()
{
    ISteamUtils* utils = SteamUtils();
    return (utils == NULL) ? SteamGameServerUtils() : utils;
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUtils_getAppID (
    JNIEnv* env, jclass clazz)
{
    return getUtils()->GetAppID();
}

static JNIEnv* _warningEnv;
static jobject _warningObj;
static jmethodID _warningMid;

static void warning (int severity, const char* message)
{
    _warningEnv->CallVoidMethod(
        _warningObj, _warningMid, (jint)severity, _warningEnv->NewStringUTF(message));
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUtils_setWarningMessageHook (
    JNIEnv* env, jclass clazz, jobject hook)
{
    if (_warningObj != NULL) {
        _warningEnv->DeleteGlobalRef(_warningObj);
    }
    _warningEnv = env;
    _warningObj = env->NewGlobalRef(hook);
    jclass hclazz = env->FindClass("com/threerings/froth/SteamUtils$WarningMessageHook");
    _warningMid = env->GetMethodID(hclazz, "warning", "(ILjava/lang/String;)V");
    getUtils()->SetWarningMessageHook(warning);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamUtils_isOverlayEnabled (
    JNIEnv* env, jclass clazz)
{
    return getUtils()->IsOverlayEnabled();
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamUtils_overlayNeedsPresent (
    JNIEnv* env, jclass clazz)
{
    return getUtils()->BOverlayNeedsPresent();
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamUtils_nativeSetOverlayNotificationPosition (
    JNIEnv* env, jclass clazz, jint position)
{
    getUtils()->SetOverlayNotificationPosition((ENotificationPosition)position);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamUtils_nativeShowFloatingGamepadTextInput (
    JNIEnv* env, jclass clazz, jint keyboardMode, jint x, jint y, jint w, jint h)
{
  return getUtils()->ShowFloatingGamepadTextInput((EFloatingGamepadTextInputMode)keyboardMode,
      x, y, w, h);
}
