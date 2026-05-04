import React, { useState, useEffect } from 'react';
import api from '../api';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend } from 'recharts';

const Statistici = () => {
    const [data, setData] = useState(null);
    const [zile, setZile] = useState(30);
    const [categorie, setCategorie] = useState(""); // Starea pentru categoria aleasă
    const [loading, setLoading] = useState(true);

    // Listă de categorii (poate fi adusă și din backend/enums)
    const categoriiDisponibile = ["ADOPTIE", "PIERDUT", "GASIT", "PROBLEMA"];

    useEffect(() => {
        const fetchStatistici = async () => {
            setLoading(true);
            try {
                // Trimitem atât zilele cât și categoria selectată
                const params = { zile };
                if (categorie) params.categorie = categorie;

                const response = await api.get('/anunturi/statistici', { params });

                const formattedCats = Object.entries(response.data.perCategorie).map(([name, value]) => ({ name, value }));
                const formattedLocs = Object.entries(response.data.perLocalitate).map(([name, value]) => ({ name, value }));

                setData({ perCategorie: formattedCats, perLocalitate: formattedLocs });
            } catch (err) {
                console.error("Eroare statistici:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchStatistici();
    }, [zile, categorie]); // Re-execută la schimbarea perioadei SAU a categoriei

    return (
        <div className="statistici-container">
            <div className="filters" style={{ display: 'flex', gap: '20px', marginBottom: '20px' }}>
                <div>
                    <label>Perioada: </label>
                    <select value={zile} onChange={(e) => setZile(Number(e.target.value))}>
                        <option value={30}>30 zile</option>
                        <option value={90}>90 zile</option>
                        <option value={360}>360 zile</option>
                    </select>
                </div>

                <div>
                    <label>Categoria: </label>
                    <select value={categorie} onChange={(e) => setCategorie(e.target.value)}>
                        <option value="">Toate categoriile</option>
                        {categoriiDisponibile.map(cat => (
                            <option key={cat} value={cat}>{cat}</option>
                        ))}
                    </select>
                </div>
            </div>

            {loading ? (
                <p>Se încarcă datele...</p>
            ) : (
                <div className="charts-grid" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
                    {/* Diagrama de localități devine mult mai relevantă când alegi o categorie */}
                    <div style={{ height: 400, background: '#f9f9f9', padding: '15px', borderRadius: '8px' }}>
                        <h3>Localități cu cele mai multe anunțuri ({categorie || "General"})</h3>
                        <ResponsiveContainer width="100%" height="100%">
                            <BarChart data={data?.perLocalitate}>
                                <XAxis dataKey="name" />
                                <YAxis />
                                <Tooltip />
                                <Bar dataKey="value" fill="#00C49F" name="Nr. Anunțuri" />
                            </BarChart>
                        </ResponsiveContainer>
                    </div>

                    <div style={{ height: 400, background: '#f9f9f9', padding: '15px', borderRadius: '8px' }}>
                        <h3>Ponderea categoriilor</h3>
                        <ResponsiveContainer width="100%" height="100%">
                            <PieChart>
                                <Pie data={data?.perCategorie} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={80} label>
                                    {data?.perCategorie.map((_, i) => <Cell key={i} fill={['#0088FE', '#00C49F', '#FFBB28'][i % 3]} />)}
                                </Pie>
                                <Tooltip />
                                <Legend />
                            </PieChart>
                        </ResponsiveContainer>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Statistici;