package com.google.android.libraries.gsa.d.a;

import android.util.Property;

final class SlidingPanelLayoutProperty extends Property {

    SlidingPanelLayoutProperty(Class cls, String str) {
        super(cls, str);
    }

    public Object get(Object obj) {
        return ((SlidingPanelLayout) obj).uoC;
    }

    public void set(Object obj, Object obj2) {
        ((SlidingPanelLayout) obj).BM((Integer) obj2);
    }
}
