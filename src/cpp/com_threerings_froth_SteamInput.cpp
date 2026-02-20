//
// JNI bindings for the ISteamInput interface.

#include <steam/steam_api.h>

#include "com_threerings_froth_SteamInput.h"

// Cached JNI references for data classes (initialized on first use)
static jclass _digitalActionDataClass = NULL;
static jmethodID _digitalActionDataSetValues = NULL;
static jclass _analogActionDataClass = NULL;
static jmethodID _analogActionDataSetValues = NULL;
static jclass _motionDataClass = NULL;
static jmethodID _motionDataSetValues = NULL;

static void ensureDigitalActionDataCached (JNIEnv* env)
{
    if (_digitalActionDataClass == NULL) {
        jclass clazz = env->FindClass(
            "com/threerings/froth/SteamInput$DigitalActionData");
        _digitalActionDataClass = (jclass)env->NewGlobalRef(clazz);
        _digitalActionDataSetValues = env->GetMethodID(
            _digitalActionDataClass, "setValues", "(ZZ)V");
    }
}

static void ensureAnalogActionDataCached (JNIEnv* env)
{
    if (_analogActionDataClass == NULL) {
        jclass clazz = env->FindClass(
            "com/threerings/froth/SteamInput$AnalogActionData");
        _analogActionDataClass = (jclass)env->NewGlobalRef(clazz);
        _analogActionDataSetValues = env->GetMethodID(
            _analogActionDataClass, "setValues", "(IFFZ)V");
    }
}

static void ensureMotionDataCached (JNIEnv* env)
{
    if (_motionDataClass == NULL) {
        jclass clazz = env->FindClass(
            "com/threerings/froth/SteamInput$MotionData");
        _motionDataClass = (jclass)env->NewGlobalRef(clazz);
        _motionDataSetValues = env->GetMethodID(
            _motionDataClass, "setValues", "(FFFFFFFFFF)V");
    }
}

// ---- Lifecycle ----

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nInit (
    JNIEnv* env, jclass clazz, jboolean explicitlyCallRunFrame)
{
    return (jboolean)SteamInput()->Init(explicitlyCallRunFrame);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nShutdown (
    JNIEnv* env, jclass clazz)
{
    return (jboolean)SteamInput()->Shutdown();
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nSetInputActionManifestFilePath (
    JNIEnv* env, jclass clazz, jstring path)
{
    const char* str = env->GetStringUTFChars(path, NULL);
    jboolean result = (jboolean)SteamInput()->SetInputActionManifestFilePath(str);
    env->ReleaseStringUTFChars(path, str);
    return result;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nRunFrame (
    JNIEnv* env, jclass clazz)
{
    SteamInput()->RunFrame();
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nBWaitForData (
    JNIEnv* env, jclass clazz, jboolean waitForever, jint timeout)
{
    return (jboolean)SteamInput()->BWaitForData(waitForever, (uint32)timeout);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nBNewDataAvailable (
    JNIEnv* env, jclass clazz)
{
    return (jboolean)SteamInput()->BNewDataAvailable();
}

// ---- Controllers ----

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nGetConnectedControllers (
    JNIEnv* env, jclass clazz, jlongArray handlesOut)
{
    InputHandle_t handles[STEAM_INPUT_MAX_COUNT];
    int count = SteamInput()->GetConnectedControllers(handles);

    // Copy native uint64 handles into the Java long array
    jlong jhandles[STEAM_INPUT_MAX_COUNT];
    for (int ii = 0; ii < count; ii++) {
        jhandles[ii] = (jlong)handles[ii];
    }
    env->SetLongArrayRegion(handlesOut, 0, count, jhandles);
    return (jint)count;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nEnableDeviceCallbacks (
    JNIEnv* env, jclass clazz)
{
    SteamInput()->EnableDeviceCallbacks();
}

// ---- Action Sets ----

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamInput_nGetActionSetHandle (
    JNIEnv* env, jclass clazz, jstring actionSetName)
{
    const char* str = env->GetStringUTFChars(actionSetName, NULL);
    jlong result = (jlong)SteamInput()->GetActionSetHandle(str);
    env->ReleaseStringUTFChars(actionSetName, str);
    return result;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nActivateActionSet (
    JNIEnv* env, jclass clazz, jlong inputHandle, jlong actionSetHandle)
{
    SteamInput()->ActivateActionSet(
        (InputHandle_t)inputHandle, (InputActionSetHandle_t)actionSetHandle);
}

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamInput_nGetCurrentActionSet (
    JNIEnv* env, jclass clazz, jlong inputHandle)
{
    return (jlong)SteamInput()->GetCurrentActionSet((InputHandle_t)inputHandle);
}

// ---- Action Set Layers ----

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nActivateActionSetLayer (
    JNIEnv* env, jclass clazz, jlong inputHandle, jlong actionSetLayerHandle)
{
    SteamInput()->ActivateActionSetLayer(
        (InputHandle_t)inputHandle, (InputActionSetHandle_t)actionSetLayerHandle);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nDeactivateActionSetLayer (
    JNIEnv* env, jclass clazz, jlong inputHandle, jlong actionSetLayerHandle)
{
    SteamInput()->DeactivateActionSetLayer(
        (InputHandle_t)inputHandle, (InputActionSetHandle_t)actionSetLayerHandle);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nDeactivateAllActionSetLayers (
    JNIEnv* env, jclass clazz, jlong inputHandle)
{
    SteamInput()->DeactivateAllActionSetLayers((InputHandle_t)inputHandle);
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nGetActiveActionSetLayers (
    JNIEnv* env, jclass clazz, jlong inputHandle, jlongArray handlesOut)
{
    InputActionSetHandle_t handles[STEAM_INPUT_MAX_ACTIVE_LAYERS];
    int count = SteamInput()->GetActiveActionSetLayers((InputHandle_t)inputHandle, handles);

    jlong jhandles[STEAM_INPUT_MAX_ACTIVE_LAYERS];
    for (int ii = 0; ii < count; ii++) {
        jhandles[ii] = (jlong)handles[ii];
    }
    env->SetLongArrayRegion(handlesOut, 0, count, jhandles);
    return (jint)count;
}

// ---- Digital Actions ----

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamInput_nGetDigitalActionHandle (
    JNIEnv* env, jclass clazz, jstring actionName)
{
    const char* str = env->GetStringUTFChars(actionName, NULL);
    jlong result = (jlong)SteamInput()->GetDigitalActionHandle(str);
    env->ReleaseStringUTFChars(actionName, str);
    return result;
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nGetDigitalActionData (
    JNIEnv* env, jclass clazz, jlong inputHandle, jlong digitalActionHandle, jobject data)
{
    InputDigitalActionData_t actionData = SteamInput()->GetDigitalActionData(
        (InputHandle_t)inputHandle, (InputDigitalActionHandle_t)digitalActionHandle);

    ensureDigitalActionDataCached(env);
    env->CallVoidMethod(data, _digitalActionDataSetValues,
        (jboolean)actionData.bState, (jboolean)actionData.bActive);
    return JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nGetDigitalActionOrigins (
    JNIEnv* env, jclass clazz, jlong inputHandle, jlong actionSetHandle,
    jlong digitalActionHandle, jintArray originsOut)
{
    EInputActionOrigin origins[STEAM_INPUT_MAX_ORIGINS];
    int count = SteamInput()->GetDigitalActionOrigins(
        (InputHandle_t)inputHandle, (InputActionSetHandle_t)actionSetHandle,
        (InputDigitalActionHandle_t)digitalActionHandle, origins);

    jint jorigins[STEAM_INPUT_MAX_ORIGINS];
    for (int ii = 0; ii < count; ii++) {
        jorigins[ii] = (jint)origins[ii];
    }
    env->SetIntArrayRegion(originsOut, 0, count, jorigins);
    return (jint)count;
}

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamInput_nGetStringForDigitalActionName (
    JNIEnv* env, jclass clazz, jlong digitalActionHandle)
{
    const char* str = SteamInput()->GetStringForDigitalActionName(
        (InputDigitalActionHandle_t)digitalActionHandle);
    return (str != NULL) ? env->NewStringUTF(str) : NULL;
}

// ---- Analog Actions ----

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamInput_nGetAnalogActionHandle (
    JNIEnv* env, jclass clazz, jstring actionName)
{
    const char* str = env->GetStringUTFChars(actionName, NULL);
    jlong result = (jlong)SteamInput()->GetAnalogActionHandle(str);
    env->ReleaseStringUTFChars(actionName, str);
    return result;
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nGetAnalogActionData (
    JNIEnv* env, jclass clazz, jlong inputHandle, jlong analogActionHandle, jobject data)
{
    InputAnalogActionData_t actionData = SteamInput()->GetAnalogActionData(
        (InputHandle_t)inputHandle, (InputAnalogActionHandle_t)analogActionHandle);

    ensureAnalogActionDataCached(env);
    env->CallVoidMethod(data, _analogActionDataSetValues,
        (jint)actionData.eMode, (jfloat)actionData.x, (jfloat)actionData.y,
        (jboolean)actionData.bActive);
    return JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nGetAnalogActionOrigins (
    JNIEnv* env, jclass clazz, jlong inputHandle, jlong actionSetHandle,
    jlong analogActionHandle, jintArray originsOut)
{
    EInputActionOrigin origins[STEAM_INPUT_MAX_ORIGINS];
    int count = SteamInput()->GetAnalogActionOrigins(
        (InputHandle_t)inputHandle, (InputActionSetHandle_t)actionSetHandle,
        (InputAnalogActionHandle_t)analogActionHandle, origins);

    jint jorigins[STEAM_INPUT_MAX_ORIGINS];
    for (int ii = 0; ii < count; ii++) {
        jorigins[ii] = (jint)origins[ii];
    }
    env->SetIntArrayRegion(originsOut, 0, count, jorigins);
    return (jint)count;
}

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamInput_nGetStringForAnalogActionName (
    JNIEnv* env, jclass clazz, jlong analogActionHandle)
{
    const char* str = SteamInput()->GetStringForAnalogActionName(
        (InputAnalogActionHandle_t)analogActionHandle);
    return (str != NULL) ? env->NewStringUTF(str) : NULL;
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nStopAnalogActionMomentum (
    JNIEnv* env, jclass clazz, jlong inputHandle, jlong analogActionHandle)
{
    SteamInput()->StopAnalogActionMomentum(
        (InputHandle_t)inputHandle, (InputAnalogActionHandle_t)analogActionHandle);
}

// ---- Motion Data ----

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nGetMotionData (
    JNIEnv* env, jclass clazz, jlong inputHandle, jobject data)
{
    InputMotionData_t motionData = SteamInput()->GetMotionData((InputHandle_t)inputHandle);

    ensureMotionDataCached(env);
    env->CallVoidMethod(data, _motionDataSetValues,
        (jfloat)motionData.rotQuatX, (jfloat)motionData.rotQuatY,
        (jfloat)motionData.rotQuatZ, (jfloat)motionData.rotQuatW,
        (jfloat)motionData.posAccelX, (jfloat)motionData.posAccelY,
        (jfloat)motionData.posAccelZ,
        (jfloat)motionData.rotVelX, (jfloat)motionData.rotVelY,
        (jfloat)motionData.rotVelZ);
    return JNI_TRUE;
}

// ---- Glyphs and Strings ----

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamInput_nGetGlyphPNGForActionOrigin (
    JNIEnv* env, jclass clazz, jint origin, jint size, jint flags)
{
    const char* str = SteamInput()->GetGlyphPNGForActionOrigin(
        (EInputActionOrigin)origin, (ESteamInputGlyphSize)size, (uint32)flags);
    return (str != NULL) ? env->NewStringUTF(str) : NULL;
}

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamInput_nGetGlyphSVGForActionOrigin (
    JNIEnv* env, jclass clazz, jint origin, jint flags)
{
    const char* str = SteamInput()->GetGlyphSVGForActionOrigin(
        (EInputActionOrigin)origin, (uint32)flags);
    return (str != NULL) ? env->NewStringUTF(str) : NULL;
}

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamInput_nGetGlyphForActionOriginLegacy (
    JNIEnv* env, jclass clazz, jint origin)
{
    const char* str = SteamInput()->GetGlyphForActionOrigin_Legacy((EInputActionOrigin)origin);
    return (str != NULL) ? env->NewStringUTF(str) : NULL;
}

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamInput_nGetStringForActionOrigin (
    JNIEnv* env, jclass clazz, jint origin)
{
    const char* str = SteamInput()->GetStringForActionOrigin((EInputActionOrigin)origin);
    return (str != NULL) ? env->NewStringUTF(str) : NULL;
}

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamInput_nGetStringForXboxOrigin (
    JNIEnv* env, jclass clazz, jint origin)
{
    const char* str = SteamInput()->GetStringForXboxOrigin((EXboxOrigin)origin);
    return (str != NULL) ? env->NewStringUTF(str) : NULL;
}

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamInput_nGetGlyphForXboxOrigin (
    JNIEnv* env, jclass clazz, jint origin)
{
    const char* str = SteamInput()->GetGlyphForXboxOrigin((EXboxOrigin)origin);
    return (str != NULL) ? env->NewStringUTF(str) : NULL;
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nGetActionOriginFromXboxOrigin (
    JNIEnv* env, jclass clazz, jlong inputHandle, jint xboxOrigin)
{
    return (jint)SteamInput()->GetActionOriginFromXboxOrigin(
        (InputHandle_t)inputHandle, (EXboxOrigin)xboxOrigin);
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nTranslateActionOrigin (
    JNIEnv* env, jclass clazz, jint destinationType, jint sourceOrigin)
{
    return (jint)SteamInput()->TranslateActionOrigin(
        (ESteamInputType)destinationType, (EInputActionOrigin)sourceOrigin);
}

// ---- Haptics and LEDs ----

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nTriggerVibration (
    JNIEnv* env, jclass clazz, jlong inputHandle, jint leftSpeed, jint rightSpeed)
{
    SteamInput()->TriggerVibration(
        (InputHandle_t)inputHandle, (unsigned short)leftSpeed, (unsigned short)rightSpeed);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nTriggerVibrationExtended (
    JNIEnv* env, jclass clazz, jlong inputHandle,
    jint leftSpeed, jint rightSpeed, jint leftTriggerSpeed, jint rightTriggerSpeed)
{
    SteamInput()->TriggerVibrationExtended(
        (InputHandle_t)inputHandle,
        (unsigned short)leftSpeed, (unsigned short)rightSpeed,
        (unsigned short)leftTriggerSpeed, (unsigned short)rightTriggerSpeed);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nTriggerSimpleHapticEvent (
    JNIEnv* env, jclass clazz, jlong inputHandle, jint hapticLocation,
    jint intensity, jint gainDB, jint otherIntensity, jint otherGainDB)
{
    SteamInput()->TriggerSimpleHapticEvent(
        (InputHandle_t)inputHandle, (EControllerHapticLocation)hapticLocation,
        (uint8)intensity, (char)gainDB, (uint8)otherIntensity, (char)otherGainDB);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nSetLEDColor (
    JNIEnv* env, jclass clazz, jlong inputHandle,
    jint colorR, jint colorG, jint colorB, jint flags)
{
    SteamInput()->SetLEDColor(
        (InputHandle_t)inputHandle,
        (uint8)colorR, (uint8)colorG, (uint8)colorB, (unsigned int)flags);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nLegacyTriggerHapticPulse (
    JNIEnv* env, jclass clazz, jlong inputHandle, jint targetPad, jint durationMicroSec)
{
    SteamInput()->Legacy_TriggerHapticPulse(
        (InputHandle_t)inputHandle, (ESteamControllerPad)targetPad,
        (unsigned short)durationMicroSec);
}

JNIEXPORT void JNICALL Java_com_threerings_froth_SteamInput_nLegacyTriggerRepeatedHapticPulse (
    JNIEnv* env, jclass clazz, jlong inputHandle, jint targetPad,
    jint durationMicroSec, jint offMicroSec, jint repeat, jint flags)
{
    SteamInput()->Legacy_TriggerRepeatedHapticPulse(
        (InputHandle_t)inputHandle, (ESteamControllerPad)targetPad,
        (unsigned short)durationMicroSec, (unsigned short)offMicroSec,
        (unsigned short)repeat, (unsigned int)flags);
}

// ---- Utility ----

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nShowBindingPanel (
    JNIEnv* env, jclass clazz, jlong inputHandle)
{
    return (jboolean)SteamInput()->ShowBindingPanel((InputHandle_t)inputHandle);
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nGetInputTypeForHandle (
    JNIEnv* env, jclass clazz, jlong inputHandle)
{
    return (jint)SteamInput()->GetInputTypeForHandle((InputHandle_t)inputHandle);
}

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamInput_nGetControllerForGamepadIndex (
    JNIEnv* env, jclass clazz, jint index)
{
    return (jlong)SteamInput()->GetControllerForGamepadIndex((int)index);
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nGetGamepadIndexForController (
    JNIEnv* env, jclass clazz, jlong inputHandle)
{
    return (jint)SteamInput()->GetGamepadIndexForController((InputHandle_t)inputHandle);
}

JNIEXPORT jboolean JNICALL Java_com_threerings_froth_SteamInput_nGetDeviceBindingRevision (
    JNIEnv* env, jclass clazz, jlong inputHandle, jintArray revisionOut)
{
    int major, minor;
    bool result = SteamInput()->GetDeviceBindingRevision(
        (InputHandle_t)inputHandle, &major, &minor);
    if (result) {
        jint values[2] = { (jint)major, (jint)minor };
        env->SetIntArrayRegion(revisionOut, 0, 2, values);
    }
    return (jboolean)result;
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nGetRemotePlaySessionID (
    JNIEnv* env, jclass clazz, jlong inputHandle)
{
    return (jint)SteamInput()->GetRemotePlaySessionID((InputHandle_t)inputHandle);
}

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamInput_nGetSessionInputConfigurationSettings (
    JNIEnv* env, jclass clazz)
{
    return (jint)SteamInput()->GetSessionInputConfigurationSettings();
}
