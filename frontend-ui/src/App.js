import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

const API_BASE = "/url-shortener";

const App = () => {
    const [urls, setUrls] = useState([]);
    const [form, setForm] = useState({ fullUrl: '', customAlias: '' });

    useEffect(() => { fetchUrls(); }, []);

    const fetchUrls = async () => {
        const res = await axios.get(`${API_BASE}/urls`);
        setUrls(res.data);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await axios.post(`${API_BASE}/shorten`, form);
        setForm({ fullUrl: '', customAlias: '' });
        fetchUrls();
    };

    const deleteUrl = async (alias) => {
        await axios.delete(`${API_BASE}/${alias}`);
        fetchUrls();
    };

    return (
        <div className="container">
            <h1 style={{ textAlign: 'center', color: '#333' }}>URL Shortener</h1>

            {/* Input Card */}
            <div className="card">
                <h3>Create New Link</h3>
                <form onSubmit={handleSubmit} className="input-group">
                    <input
                        placeholder="https://very-long-url.com..."
                        value={form.fullUrl}
                        onChange={e => setForm({...form, fullUrl: e.target.value})}
                        required
                    />
                    <input
                        placeholder="Alias (optional)"
                        style={{ flex: 0.4 }}
                        value={form.customAlias}
                        onChange={e => setForm({...form, customAlias: e.target.value})}
                    />
                    <button type="submit">Shorten</button>
                </form>
            </div>

            {/* List Card */}
            <div className="card">
                <h3>Your Active Links</h3>
                <table className="url-table">
                    <thead>
                        <tr>
                            <th>Short Link</th>
                            <th>Original URL</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {urls.map(u => (
                            <tr key={u.alias}>
                                <td><a href={u.shortUrl} target="_blank">{u.alias}</a></td>
                                <td style={{ color: '#666', fontSize: '0.9rem' }}>
                                    {u.fullUrl.substring(0, 30)}...
                                </td>
                                <td>
                                    <button className="delete-btn" onClick={() => deleteUrl(u.alias)}>
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default App;
