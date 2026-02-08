import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../css/dashboard.css";
import Anunturi from "././Anunturi.jsx"
import Utilizatori from "./Utilizatori.jsx";
import Loguri from "./Loguri.jsx";

export default function Dashboard() {
    const [activeTab, setActiveTab] = useState("tab1");
    const navigate = useNavigate();

    const renderContent = () => {
        switch (activeTab) {
            case "tab1":
                return <Anunturi />;
            case "tab2":
                return <Utilizatori />;
            case "tab3":
                return <Loguri />;
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
                        Utilizatori
                    </button>
                    <button
                        onClick={() => setActiveTab("tab3")}
                        className={`tab-button ${activeTab === "tab3" ? "active" : ""}`}
                    >
                        Loguri
                    </button>
                </div>


                <div className="content-box">{renderContent()}</div>



            </div>
        </div>
    );
}