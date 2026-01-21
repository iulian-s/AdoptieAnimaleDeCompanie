import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api";

export default function DetaliiUtilizator(){
    const { id } = useParams();
    const [utilizator, setUtilizator] = useState(null);
    const [judet, setJudet] = useState("");
    //const [localitate, setLocalitate] = useState("");



    useEffect(() => {
        const fetchData = async() => {
            try{
                const resUtilizator = await api.get(`/utilizator/${id}`)
                setUtilizator(resUtilizator.data)

                // const resLocalitate = await api.get(`/localitati/${resUtilizator.data.localitateId}`);
                // setLocalitate(resLocalitate.data.nume);
                // setJudet(resLocalitate.data.judet);
            } catch (err) {
            console.error(err);
        }

        };
        fetchData();
    }, [id]);

    if (!utilizator) return <div>Loading...</div>;

    return (
        <div style={{ maxWidth: "800px", margin: "0 auto", padding: "24px", fontFamily: "Arial, sans-serif" }}>

            <h2 style={{ marginBottom: "16px", fontSize: "28px", fontWeight: "bold", textAlign: "center" }}>
                {utilizator.username}
            </h2>


            {
                <>
                    <div style={{ width: "100%", height: "45px", overflow: "hidden", borderRadius: "12px", marginBottom: "12px" }}>
                        <img
                            src={`http://localhost:8080${utilizator.avatar}`}
                            alt={`poza-nu-exista`}
                            style={{ width: "100%", height: "100%", objectFit: "contain" }}
                        />
                    </div>

                </>
            }

            {/* Date user */}
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Id: {utilizator.id}

            </p>
            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Email: {utilizator.email}
            </p>

            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Rol: {utilizator.rol}
            </p>

            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Nume: {utilizator.nume}
            </p>

            {/*<p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>*/}
            {/*    Id-ul locatiei: {utilizator.localitateId}, Judet: {judet}, Localitate: {localitate}*/}
            {/*</p>*/}

            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Telefon: {utilizator.telefon}
            </p>

            <p style={{ lineHeight: "1.6", fontSize: "16px", color: "#333", textAlign: "justify" }}>
                Data crearii contului: {utilizator.dataCreare}
            </p>

            <div style={{
                lineHeight: "1.6",
                fontSize: "16px",
                color: "#333",
                textAlign: "justify",
                display: "flex",
                gap: "5px"
            }}>
                Anunturi:

                {/* Parcurgem array-ul de ID-uri și cream elemente span interactive */}
                {utilizator.anuntIds.length > 0 ? (
                    utilizator.anuntIds.map((anuntId, index) => (
                        <>
                <span
                    key={anuntId}
                    onClick={() => window.open(`http://localhost:3000/anunt/${anuntId}`, "_blank")}
                    style={{
                        color: "#007bff",
                        cursor: "pointer",
                        textDecoration: "underline",
                        fontWeight: "bold"
                    }}
                >
                    {anuntId}
                </span>

                            {/* Virgula, separator, dar nu si pt ultimul elem */}
                            {index < utilizator.anuntIds.length - 1 && <span>, </span>}
                        </>
                    ))
                ) : (
                    // Mesaj daca nu exista anunturi
                    <span>Niciun anunt creat.</span>
                )}
            </div>

        </div>
    );


}