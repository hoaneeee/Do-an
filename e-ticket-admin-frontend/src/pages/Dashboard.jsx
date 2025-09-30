import { useEffect, useState } from 'react'
import { getDashboardSummary } from '../api/dashboard'

const toNumber = v => (typeof v === 'string' ? Number(v) : (v ?? 0))
const fmtVND = n => toNumber(n).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })

function Stat({ title, value }) {
  return (
    <div className="col">
      <div className="p-3 border rounded-3 bg-white">
        <div className="text-muted small">{title}</div>
        <div className="fs-5 fw-bold">{value}</div>
      </div>
    </div>
  )
}

export default function Dashboard() {
  const [data, setData] = useState(null)
  const [err, setErr] = useState('')

  useEffect(() => {
    getDashboardSummary()
      .then(setData)
      .catch(e => setErr(e?.response?.data?.message || e?.message || 'Load dashboard failed'))
  }, [])

  if (err) return <div className="alert alert-danger">{err}</div>
  if (!data) return <div>Loading...</div>

  const s = data.summary || {}

  return (
    <div>
      <h3 className="mb-3">Dashboard</h3>

      <div className="row g-3">
        <Stat title="Doanh thu" value={fmtVND(s.totalRevenue)} />
        <Stat title="Đơn đã thanh toán" value={s.totalPaidOrders ?? 0} />
        <Stat title="Vé đã bán" value={s.totalTicketsSold ?? 0} />
        <Stat title="Sự kiện Public" value={s.publishedEvents ?? 0} />
        <Stat title="Sắp diễn ra" value={s.upcomingEvents ?? 0} />
      </div>

      <h5 className="mt-4">Doanh thu (14 ngày)</h5>
      <ul className="small">
        {(data.revenueByDate || []).map(p => (
          <li key={p.date}>{p.date}: {fmtVND(p.amount)}</li>
        ))}
      </ul>

      <h5 className="mt-3">Top Events</h5>
      <ul className="small">
        {(data.topEvents || []).map(t => (
          <li key={t.eventId}>
            {t.title} — {fmtVND(t.revenue)} ({t.tickets} vé)
          </li>
        ))}
      </ul>
    </div>
  )
}
