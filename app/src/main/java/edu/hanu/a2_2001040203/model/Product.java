package edu.hanu.a2_2001040203.model;

public class Product {
    public Product(int id, String thumbnail, String name, long price, long sumPrice, int quantity) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.price = price;
        this.sumPrice = sumPrice;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(long sumPrice) {
        this.sumPrice = sumPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", thumbnail='" + thumbnail + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", sumPrice=" + sumPrice +
                ", quantity=" + quantity +
                '}';
    }

    private int id;
    private String thumbnail;
    private String name;
    private long price;
    private long sumPrice;
    private int quantity;


}

