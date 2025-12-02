import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api";

export default function DetaliiAnunt() {
    const { id } = useParams();
    const [anunt, setAnunt] = useState(null);
    const [currentImg, setCurrentImg] = useState(0);
    const [username, setUsername] = useState("");
    const [nume, setNume] = useState("");
    const [judet, setJudet] = useState("");


    useEffect(() => {
        const fetchData = async () => {
            try {
                const resAnunt = await api.get(`/anunturi/${id}`);
                setAnunt(resAnunt.data);

                const resUser = await api.get(`/utilizator/${resAnunt.data.utilizatorId}`);
                setUsername(resUser.data.username);

                const resLocalitate = await api.get(`/localitati/${resAnunt.data.locatieId}`);
                setNume(resLocalitate.data.nume);

                const resJudet = await api.get(`/localitati/${resAnunt.data.locatieId}`);
                setJudet(resLocalitate.data.judet);

                // poți continua cu alte fetch-uri
            } catch (err) {
                console.error(err);
            }
        };

        fetchData();
    }, [id]);

    if (!anunt) return <div>Loading...</div>;

    return (
        <div style={{ maxWidth: "800px", margin: "0 auto", padding: "24px", fontFamily: "Arial, sans-serif" }}>

            <h2 style={{ marginBottom: "16px", fontSize: "28px", fontWeight: "bold", textAlign: "center" }}>
                {anunt.titlu}
            </h2>


            {anunt.listaImagini.length > 0 && (
                <>
                    <div style={{ width: "100%", height: "450px", overflow: "hidden", borderRadius: "12px", marginBottom: "12px" }}>
                        <img
                            src={`http://localhost:8080${anunt.listaImagini[currentImg]}`}
                            alt={`poza-${currentImg}`}
                            style={{ width: "100%", height: "100%", objectFit: "contain", objectPosition: "center" }}
                        />
                    </div>


                    <div style={{ display: "flex", gap: "8px", marginBottom: "24px" }}>
                        {anunt.listaImagini.map((imgUrl, index) => (
                            <img
                                key={index}
                                src={`http://localhost:8080${imgUrl}`}
                                alt={`thumb-${index}`}
                                onClick={() => setCurrentImg(index)}
                                style={{
                                    width: "80px",
                                    height: "80px",
                                    objectFit: "cover",
                                    borderRadius: "8px",
                                    border: currentImg === index ? "3px solid #3b82f6" : "2px solid #ccc",
                                    cursor: "pointer",
                                    transition: "all 0.2s ease"
                                }}
                            />
                        ))}
                    </div>
                </>
            )}

            {/* Descriere */}
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Descriere: {anunt.descriere}
            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Specie: {anunt.specie}
            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Rasa: {anunt.rasa}
            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Gen: {anunt.gen}
            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Varsta: {anunt.varsta}
            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                ID-ul utilizatorului care a postat:
                <span
                    key={id}
                    onClick={() => window.open(`http://localhost:3000/utilizator/${anunt.utilizatorId}`, "_blank")}
                    style={{
                        color: "#007bff",
                        cursor: "pointer",
                        textDecoration: "underline",
                        fontWeight: "bold"
                    }}
                >
                    {anunt.utilizatorId}
                </span>

            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Creat la data: {anunt.createdAt}
            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Actualizat la data: {anunt.updatedAt}
            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Id-ul locatiei: {anunt.locatieId}, Judet: {judet}, Localitate: {nume}
            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Stare: {anunt.stare}
            </p>
        </div>
    );
}
