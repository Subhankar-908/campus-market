import React, { useMemo, useState } from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';
import { login } from '../services/api';
import { isAuthenticated, setToken } from '../services/auth';

function extractToken(payload) {
  if (!payload) return null;
  if (typeof payload === 'string') return payload;
  if (typeof payload === 'object') {
    return (
      payload.token ??
      payload.accessToken ??
      payload.jwt ??
      payload?.data?.token ??
      null
    );
  }
  return null;
}

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();

  const redirectTo = useMemo(() => {
    const from = location.state?.from?.pathname;
    return typeof from === 'string' ? from : '/products';
  }, [location.state]);

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  if (isAuthenticated()) return <Navigate to="/products" replace />;

  async function onSubmit(e) {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const data = await login(username, password);
      const token = extractToken(data);
      if (!token) {
        throw new Error(
          'Login succeeded but no token was returned. Expected { token: "..." }.'
        );
      }
      setToken(token);
      navigate(redirectTo, { replace: true });
    } catch (err) {
      const message =
        err?.response?.data?.message ||
        err?.response?.data?.error ||
        err?.message ||
        'Login failed';
      setError(message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="container" style={{ padding: '48px 0' }}>
      <div
        className="card"
        style={{
          maxWidth: 460,
          margin: '0 auto',
          padding: 22
        }}
      >
        <div style={{ fontWeight: 800, fontSize: 22 }}>Sign in</div>
        <div className="muted" style={{ marginTop: 6 }}>
          Use your backend credentials to get a JWT.
        </div>

        <div className="spacer" />

        {error ? <div className="error">{error}</div> : null}

        <form onSubmit={onSubmit} style={{ marginTop: 14 }}>
          <div style={{ display: 'grid', gap: 10 }}>
            <div>
              <div className="muted" style={{ fontSize: 12, marginBottom: 6 }}>
                Username
              </div>
              <input
                className="input"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="Enter username"
                autoComplete="username"
                required
              />
            </div>

            <div>
              <div className="muted" style={{ fontSize: 12, marginBottom: 6 }}>
                Password
              </div>
              <input
                className="input"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter password"
                autoComplete="current-password"
                required
              />
            </div>

            <button
              className="btn primary"
              type="submit"
              disabled={loading}
              style={{ width: '100%', marginTop: 4 }}
            >
              {loading ? 'Signing in…' : 'Login'}
            </button>
          </div>
        </form>

        <div className="muted" style={{ fontSize: 12, marginTop: 12 }}>
          API: <span style={{ fontFamily: 'ui-monospace, SFMono-Regular, Menlo, monospace' }}>POST /api/auth/login</span>
        </div>
      </div>
    </div>
  );
}

