import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import "../css/dashboard.css";
import { jwtDecode } from "jwt-decode";
import Anunturi from "././Anunturi.jsx"

export default function Dashboard() {
    const [activeTab, setActiveTab] = useState("tab1");
    const navigate = useNavigate();

    const renderContent = () => {
        switch (activeTab) {
            case "tab1":
                return <Anunturi />;
            case "tab2":
                return <div>Continutul tabului 2</div>;
            case "tab3":
                return <div>Continutul tabului 3</div>;
            default:
                return null;
        }
    };
    const handleLogout = () => {
        alert("Ai fost delogat!");
        localStorage.removeItem("token");
        navigate("/login");

    };
    return (
        <div className="dashboard-container">

            <div className="dashboard-card">
                <h1 className="dashboard-title">Dashboard</h1>
                <div className="logout-area">
                    <button onClick={handleLogout} className="logout-button">
                        Logout
                    </button>
                </div>


                <div className="tabs">
                    <button
                        onClick={() => setActiveTab("tab1")}
                        className={`tab-button ${activeTab === "tab1" ? "active" : ""}`}
                    >
                        Anunturi
                    </button>
                    <button
                        onClick={() => setActiveTab("tab2")}
                        className={`tab-button ${activeTab === "tab2" ? "active" : ""}`}
                    >
                        Tab 2
                    </button>
                    <button
                        onClick={() => setActiveTab("tab3")}
                        className={`tab-button ${activeTab === "tab3" ? "active" : ""}`}
                    >
                        Tab 3
                    </button>
                </div>


                <div className="content-box">{renderContent()}</div>



            </div>
        </div>
    );
}