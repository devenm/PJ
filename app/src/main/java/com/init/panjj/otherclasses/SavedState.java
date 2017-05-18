package com.init.panjj.otherclasses;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference.BaseSavedState;

public class SavedState extends BaseSavedState {
    public static final Creator<SavedState> CREATOR;
    public int stateToSave;

    /* renamed from: com.init.panj.clases.SavedState.1 */
    static class C05321 implements Creator<SavedState> {
        C05321() {
        }

        public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    }

    public SavedState(Parcelable superState) {
        super(superState);
    }

    public SavedState(Parcel in) {
        super(in);
        this.stateToSave = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(this.stateToSave);
    }

    static {
        CREATOR = new C05321();
    }
}
