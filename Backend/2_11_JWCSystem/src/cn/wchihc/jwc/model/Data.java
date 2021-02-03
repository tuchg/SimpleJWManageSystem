package cn.wchihc.jwc.model;

import java.util.List;

public class Data<T> {

    /**
     * total : 1
     * items : [{},{}]
     */

    private int total;
    private List<T> items;

    public Data() {
    }

    public Data(int total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
