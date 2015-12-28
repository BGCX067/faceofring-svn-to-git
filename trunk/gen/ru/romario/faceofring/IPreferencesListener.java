/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Development\\_WorkplaceEclipse\\Face of Ring\\src\\ru\\romario\\faceofring\\IPreferencesListener.aidl
 */
package ru.romario.faceofring;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
// Declare the interface.

public interface IPreferencesListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements ru.romario.faceofring.IPreferencesListener
{
private static final java.lang.String DESCRIPTOR = "ru.romario.faceofring.IPreferencesListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IPreferencesListener interface,
 * generating a proxy if needed.
 */
public static ru.romario.faceofring.IPreferencesListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof ru.romario.faceofring.IPreferencesListener))) {
return ((ru.romario.faceofring.IPreferencesListener)iin);
}
return new ru.romario.faceofring.IPreferencesListener.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onPreferencesChanged:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onPreferencesChanged(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements ru.romario.faceofring.IPreferencesListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void onPreferencesChanged(java.lang.String key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
mRemote.transact(Stub.TRANSACTION_onPreferencesChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onPreferencesChanged = (IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onPreferencesChanged(java.lang.String key) throws android.os.RemoteException;
}
