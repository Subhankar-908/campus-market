import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { clearToken, isAuthenticated } from '../services/auth';

export default function Navbar() {
  const navigate = useNavigate();
  const authed = isAuthenticated();

  function onLogout() {
    clearToken();
    navigate('/login', { replace: true });
  }

  return (
    <div
      style={{
        position: 'sticky',
        top: 0,
        zIndex: 10,
        backdropFilter: 'blur(10px)',
        background: 'rgba(11, 18, 32, 0.65)',
        borderBottom: '1px solid rgba(255,255,255,0.10)'
      }}
    >
      <div
        className="container"
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          padding: '14px 0'
        }}
      >
        <div className="row" style={{ gap: 10 }}>
          <div
            style={{
              width: 30,
              height: 30,
              borderRadius: 10,
              background: 'rgba(91, 140, 255, 0.20)',
              border: '1px solid rgba(91, 140, 255, 0.35)'
            }}
          />
        <div>
            <div style={{ fontWeight: 800, letterSpacing: 0.2 }}>
              Campus Marketplace
            </div>
            <div className="muted" style={{ fontSize: 12 }}>
              React + Spring Boot
            </div>
          </div>
        </div>

        <div className="row">
          {authed ? (
            <>
              <Link className="btn" to="/products">
                Products
              </Link>
              <button className="btn danger" onClick={onLogout}>
                Logout
              </button>
            </>
          ) : (
            <Link className="btn" to="/login">
              Login
            </Link>
          )}
        </div>
      </div>
    </div>
  );
}

