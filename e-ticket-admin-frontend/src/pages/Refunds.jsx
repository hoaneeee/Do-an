import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import {
  createRefund, listRefundsByOrder,
  approveRefund, rejectRefund, markRefundPaid
} from '../api/refunds';

export default function Refunds(){
  const [sp] = useSearchParams();
  const orderId = Number(sp.get('orderId'));   // /admin/refunds?orderId=123
  const [list, setList] = useState([]);
  const [amount, setAmount] = useState('');
  const [reason, setReason] = useState('');

  const refresh = ()=> listRefundsByOrder(orderId).then(setList);
  useEffect(()=>{ if(orderId) refresh(); },[orderId]);

  async function onCreate(){
    if(!amount) return alert('Nhập số tiền');
    await createRefund(orderId, Number(amount), reason);
    setAmount(''); setReason(''); await refresh();
  }

  return (
    <div>
      <h3>Refunds for Order #{orderId}</h3>

      <div className="row g-2 mb-3" style={{maxWidth:600}}>
        <div className="col-4">
          <input className="form-control" placeholder="Amount"
            type="number" value={amount} onChange={e=>setAmount(e.target.value)} />
        </div>
        <div className="col">
          <input className="form-control" placeholder="Reason"
            value={reason} onChange={e=>setReason(e.target.value)} />
        </div>
        <div className="col-auto">
          <button className="btn btn-primary" onClick={onCreate}>Create</button>
        </div>
      </div>

      <table className="table table-sm">
        <thead><tr><th>ID</th><th>Amount</th><th>Status</th><th>Reason</th><th>At</th><th/></tr></thead>
        <tbody>
        {list.map(r=>(
          <tr key={r.id}>
            <td>{r.id}</td>
            <td>{(r.amount||0).toLocaleString('vi-VN')}đ</td>
            <td><span className={`badge ${
              r.status==='PENDING'?'bg-warning':
              r.status==='APPROVED'?'bg-info':
              r.status==='PAID'?'bg-success': 'bg-secondary'
            }`}>{r.status}</span></td>
            <td>{r.reason}</td>
            <td>{new Date(r.createdAt).toLocaleString()}</td>
            <td className="text-end">
              {r.status==='PENDING' && <>
                <button className="btn btn-outline-success btn-sm me-1" onClick={async()=>{ await approveRefund(r.id); await refresh(); }}>Approve</button>
                <button className="btn btn-outline-danger btn-sm me-1"  onClick={async()=>{ await rejectRefund(r.id); await refresh(); }}>Reject</button>
              </>}
              {r.status==='APPROVED' &&
                <button className="btn btn-outline-primary btn-sm" onClick={async()=>{ await markRefundPaid(r.id); await refresh(); }}>Mark paid</button>}
            </td>
          </tr>
        ))}
        </tbody>
      </table>
    </div>
  );
}
