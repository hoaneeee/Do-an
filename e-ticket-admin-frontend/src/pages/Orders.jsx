import { useEffect, useState } from "react";
import { listOrders, cancelOrder } from "../api/orders";
import { Link } from "react-router-dom";

export default function Orders() {
  const [page, setPage] = useState(0);
  const [data, setData] = useState(null);
  const refresh = () => listOrders(page, 10).then(setData);
  useEffect(() => {
    refresh();
  }, [page]);

  return (
    <div>
      <h3>Orders</h3>
      <table className="table table-sm align-middle">
        <thead>
          <tr>
            <th>Code</th>
            <th>Total</th>
            <th>Status</th>
            <th>Created</th>
            <th />
          </tr>
        </thead>
        <tbody>
          {data?.content?.map((o) => (
            <tr key={o.id}>
              <td>{o.code}</td>
              <td>{o.total.toLocaleString("vi-VN")}đ</td>
              <td>{o.status}</td>
              <td>{new Date(o.createdAt).toLocaleString()}</td>
              <td className="text-end">
                {o.status !== "CANCELLED" && (
                  <button
                    className="btn btn-outline-danger btn-sm me-1"
                    onClick={async () => {
                      await cancelOrder(o.code);
                      await refresh();
                    }}
                  >
                    Cancel
                  </button>
                )}
                <Link
                  to={`/admin/refunds?orderId=${o.id}`}
                  className="btn btn-outline-primary btn-sm"
                >
                  Refunds
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="d-flex gap-2">
        <button
          className="btn btn-light"
          disabled={page <= 0}
          onClick={() => setPage((p) => p - 1)}
        >
          Prev
        </button>
        <button
          className="btn btn-light"
          disabled={page >= data?.totalPages - 1}
          onClick={() => setPage((p) => p + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
}
