import React, { useEffect, useMemo, useState } from 'react';
import { fetchProducts } from '../services/api';

function normalizeProducts(data) {
  if (!data) return [];
  if (Array.isArray(data)) return data;
  if (Array.isArray(data.products)) return data.products;
  if (Array.isArray(data.items)) return data.items;
  if (Array.isArray(data.content)) return data.content; // Spring Data Page
  return [];
}

export default function Products() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [products, setProducts] = useState([]);

  useEffect(() => {
    let cancelled = false;
    async function run() {
      setLoading(true);
      setError('');
      try {
        const data = await fetchProducts();
        if (cancelled) return;
        setProducts(normalizeProducts(data));
      } catch (err) {
        if (cancelled) return;
        const message =
          err?.response?.data?.message ||
          err?.response?.data?.error ||
          err?.message ||
          'Failed to load products';
        setError(message);
      } finally {
        if (!cancelled) setLoading(false);
      }
    }
    run();
    return () => {
      cancelled = true;
    };
  }, []);

  const hasProducts = products.length > 0;
  const subtitle = useMemo(() => {
    if (loading) return 'Fetching products from your backend…';
    if (error) return 'Please verify the API and JWT auth.';
    if (!hasProducts) return 'No products returned yet.';
    return `${products.length} product(s)`;
  }, [loading, error, hasProducts, products.length]);

  return (
    <div className="container" style={{ padding: '24px 0 60px' }}>
      <div className="row" style={{ justifyContent: 'space-between' }}>
        <div>
          <div style={{ fontWeight: 900, fontSize: 24 }}>Products</div>
          <div className="muted" style={{ marginTop: 6 }}>
            {subtitle}
          </div>
        </div>

        <button
          className="btn"
          onClick={() => window.location.reload()}
          disabled={loading}
        >
          Refresh
        </button>
      </div>

      <div className="spacer" />

      {error ? (
        <div className="error">
          <div style={{ fontWeight: 800 }}>Couldn’t load products</div>
          <div style={{ marginTop: 6 }}>{error}</div>
          <div className="muted" style={{ marginTop: 10, fontSize: 12 }}>
            Endpoint: <span style={{ fontFamily: 'ui-monospace, SFMono-Regular, Menlo, monospace' }}>GET /api/products</span>
          </div>
        </div>
      ) : null}

      {loading ? (
        <div className="card" style={{ padding: 18 }}>
          Loading…
        </div>
      ) : null}

      {!loading && !error && !hasProducts ? (
        <div className="card">
          <div style={{ fontWeight: 800 }}>No products found</div>
          <div className="muted" style={{ marginTop: 6 }}>
            Your API returned an empty list. Add products in the backend and
            refresh.
          </div>
        </div>
      ) : null}

      {!loading && !error && hasProducts ? (
        <div
          style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))',
            gap: 12
          }}
        >
          {products.map((p, idx) => {
            const id = p?.id ?? p?.productId ?? idx;
            const name = p?.name ?? p?.title ?? `Product #${idx + 1}`;
            const price =
              p?.price != null
                ? Number(p.price)
                : p?.cost != null
                  ? Number(p.cost)
                  : null;
            const description = p?.description ?? p?.details ?? '';

            return (
              <div key={id} className="card" style={{ padding: 16 }}>
                <div style={{ fontWeight: 900 }}>{name}</div>
                <div className="muted" style={{ marginTop: 6, fontSize: 13 }}>
                  {description || '—'}
                </div>
                <div className="row" style={{ marginTop: 12, justifyContent: 'space-between' }}>
                  <div className="muted" style={{ fontSize: 12 }}>
                    ID: <span style={{ fontFamily: 'ui-monospace, SFMono-Regular, Menlo, monospace' }}>{String(id)}</span>
                  </div>
                  <div style={{ fontWeight: 900 }}>
                    {price != null && !Number.isNaN(price) ? `₹${price}` : '—'}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      ) : null}
    </div>
  );
}

