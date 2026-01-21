import {useEffect, useState} from "react";
import api from "../api";
import "../css/tab1.css";

export default function Loguri(){
    const [loguri, setLoguri] = useState([]);

    useEffect(() => {
        api.get("/logs")
            .then((res) => setLoguri(res.data))
            .catch((err) => console.error(err));
    }, []);

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Endpoint</th>
                    <th>Metoda</th>
                    <th>Status</th>
                    <th>Timestamp</th>

                </tr>
                </thead>
                <tbody>
                {loguri
                    .toSorted((a, b) => new Date(b.timestamp) - new Date(a.timestamp))
                    .map((a)=> (
                    <tr key={a.id}>
                        <td>{a.id}</td>
                        <td>{a.username}</td>
                        <td>{a.endpoint}</td>
                        <td>{a.metoda}</td>
                        <td>{a.status}</td>
                        <td>{a.timestamp}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}