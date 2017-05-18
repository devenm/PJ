package com.init.panjj.communicate;

import com.init.panjj.model.ItemBean;
import java.util.ArrayList;

public interface Communicator {
    void data(Object obj);

    void data(ArrayList<ItemBean> arrayList, ArrayList<ItemBean> arrayList2);
}
