package order;

import java.time.Instant;
import java.util.Objects;

public class OrderStatusEvent {

	private String status;
	private Instant date;

	public OrderStatusEvent() {
	}

	public OrderStatusEvent(String status, Instant date) {
		this.status = status;
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public OrderStatusEvent setStatus(String status) {
		this.status = status;
		return this;
	}

	public Instant getDate() {
		return date;
	}

	public OrderStatusEvent setDate(Instant date) {
		this.date = date;
		return this;
	}

	@Override
	public String toString() {
		return "OrderStatusEvent{" + "status='" + status + '\'' + ", date=" + date + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OrderStatusEvent that = (OrderStatusEvent) o;
		return Objects.equals(status, that.status) && Objects.equals(date, that.date);
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, date);
	}
}
