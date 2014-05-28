//
// $Id$

#include <steam/steam_api.h>

#include "com_threerings_froth_SteamController.h"

/** A static, reusabled controller state object. */
static SteamControllerState_t _controllerState;

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamController_init (
    JNIEnv* env, jclass clazz, jstring file)
{
    const char* str = env->GetStringUTFChars(file, NULL);
    bool retval = SteamController()->Init(str);
    env->ReleaseStringUTFChars(file, str);
    return (jboolean)retval;
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamController_shutdown (
    JNIEnv* env, jclass clazz)
{
    return (jboolean)SteamController()->Shutdown();
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamController_getControllerState (
    JNIEnv* env, jclass clazz, jint controller, jobject state)
{
    bool retval = SteamController()->GetControllerState((uint32)controller, &_controllerState);
    if (retval) {
        // TODO: make this all pre-cached for the fasterness?
        jclass stateClazz = env->FindClass("com/threerings/froth/SteamController$State");
        jmethodID stateSet = env->GetMethodID(stateClazz, "setValues", "(IJSSSS)V");
        env->CallVoidMethod(state, stateSet,
                _controllerState.unPacketNum,
                _controllerState.ulButtons,
                _controllerState.sLeftPadX,
                _controllerState.sLeftPadY,
                _controllerState.sRightPadX,
                _controllerState.sRightPadY);
    }
    return (jboolean)retval;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamController_setOverrideMode (
    JNIEnv* env, jclass clazz, jstring mode)
{
    const char* modeStr = env->GetStringUTFChars(mode, NULL);
    SteamController()->SetOverrideMode(modeStr);
    env->ReleaseStringUTFChars(mode, modeStr);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamController_nativeTriggerHapticPulse (
    JNIEnv* env, jclass clazz, jint controller, jint pad, jshort duration)
{
    SteamController()->TriggerHapticPulse(
        (uint32)controller, (ESteamControllerPad)pad, (unsigned short)duration);
}
