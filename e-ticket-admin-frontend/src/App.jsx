// src/App.jsx
import { BrowserRouter, Routes, Route, Navigate, Outlet, NavLink } from "react-router-dom";
import AdminLogin from "./pages/AdminLogin";
import Dashboard from "./pages/Dashboard";
import Events from "./pages/Events";
import Orders from "./pages/Orders";
import Tickets from "./pages/Tickets";
import Checkin from "./pages/Checkin";
import Venues from "./pages/Venues";
import SeatMapEditor from "./pages/SeatMapEditor";
import ZonePricing from "./pages/ZonePricing";
import { useAuth } from "./store/auth";
import Coupons from './pages/Coupons'
import InventoryConfig from './pages/InventoryConfigs'

// Route protected (can dang nhap )
function Protected() {
  const { token } = useAuth();
  return token ? <Outlet /> : <Navigate to="/admin/login" replace />;
}

// Layout chung cho admin
function AdminLayout() {
  const { setToken } = useAuth();

  const linkCls = ({ isActive }) =>
    `btn ${isActive ? "btn-primary" : "btn-outline-secondary"}`;

  return (
    <div className="container py-3">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h4 className="m-0">E-Ticket Admin</h4>
        <div className="d-flex gap-2">
          <NavLink className={linkCls} to="/admin">Dashboard</NavLink>
          <NavLink className={linkCls} to="/admin/events">Events</NavLink>
          <NavLink className={linkCls} to="/admin/orders">Orders</NavLink>
          <NavLink className={linkCls} to="/admin/checkin">Check-in</NavLink>
          <NavLink className={linkCls} to="/admin/venues">Venues</NavLink>
          <NavLink className={linkCls} to="/admin/seatmaps">Seat Maps</NavLink>
          <NavLink className={linkCls} to="/admin/coupons">Coupons</NavLink>
          <NavLink className={linkCls} to="/admin/inventory">Inventory</NavLink>
          <button
            className="btn btn-outline-danger"
            onClick={() => setToken(null)}
          >
            Logout
          </button>
        </div>
      </div>
      <Outlet />
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Login (public) */}
        <Route path="/admin/login" element={<AdminLogin />} />

        {/* can dang nhap */}
        <Route element={<Protected />}>
          <Route element={<AdminLayout />}>
            <Route path="/admin" element={<Dashboard />} />
            <Route path="/admin/events" element={<Events />} />
            <Route path="/admin/tickets/:eventId" element={<Tickets />} />
            <Route path="/admin/orders" element={<Orders />} />
            <Route path="/admin/checkin" element={<Checkin />} />
            <Route path="/admin/venues" element={<Venues />} />
            <Route path="/admin/seatmaps" element={<SeatMapEditor />} />
            <Route path="/admin/events/:eventId/zone-pricing" element={<ZonePricing />} />
            <Route path="/admin/coupons" element={<Coupons />} />
            <Route path="/admin/inventory" element={<InventoryConfig/>}/>

          </Route>
        </Route>

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/admin/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
