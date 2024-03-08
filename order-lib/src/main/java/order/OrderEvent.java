package order;

import java.util.Objects;

public class OrderEvent {

	private String product;
	private Integer quantity;

	public OrderEvent() {
	}

	public OrderEvent(String product, Integer quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public String getProduct() {
		return product;
	}

	public OrderEvent setProduct(String product) {
		this.product = product;
		return this;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public OrderEvent setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	@Override
	public String toString() {
		return "OrderEvent{" + "product='" + product + '\'' + ", quantity=" + quantity + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OrderEvent that = (OrderEvent) o;
		return Objects.equals(product, that.product) && Objects.equals(quantity, that.quantity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(product, quantity);
	}
}
