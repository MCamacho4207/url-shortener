import React, { useState, useEffect } from 'react';
import axios from 'axios';
import logo from './logo.svg';
import './App.css';

const API_BASE = "/url-shortener";

const App = () => {
    const [urls, setUrls] = useState([]);
    const [form, setForm] = useState({ fullUrl: '', customAlias: '' });
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => { fetchUrls(); }, []);

    const fetchUrls = async () => {
        const res = await axios.get(`${API_BASE}/urls`);
        setUrls(res.data);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        try {
            await axios.post(`${API_BASE}/shorten`, form);
            setForm({ fullUrl: '', customAlias: '' });
            fetchUrls();
        } catch (err) {
            const serverError = err.response?.data?.error || "An unexpected error occurred";
            setErrorMessage(serverError);
        }
    };


    const deleteUrl = async (alias) => {
        await axios.delete(`${API_BASE}/${alias}`);
        fetchUrls();
    };

    return (
        <div className="container">
            <img src={logo} className="App-logo" alt="logo" />
            <h1 className="main-title">URL Shortener</h1>

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
                {errorMessage && (
                        <div style={{
                            color: '#ff4d4d',
                            backgroundColor: 'rgba(255, 77, 77, 0.1)',
                            padding: '10px',
                            borderRadius: '6px',
                            marginTop: '15px',
                            fontSize: '0.9rem',
                            border: '1px solid rgba(255, 77, 77, 0.3)'
                        }}>
                            ⚠️ {errorMessage}
                        </div>
                    )}
            </div>

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
                                    {u.fullUrl.substring(0, 50)}...
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
