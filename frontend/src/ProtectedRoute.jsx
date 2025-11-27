import { useState, useEffect } from "react";
import { Navigate } from "react-router-dom";
import { jwtDecode }from "jwt-decode";

export default function ProtectedRoute({ children }) {
    const [isValid, setIsValid] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            setIsValid(false);
            return;
        }

        try {
            const decoded = jwtDecode(token);

            // verificare expirare
            if (decoded.exp * 1000 < Date.now()) {
                localStorage.removeItem("token");
                setIsValid(false);
                return;
            }

            // verificare rol
            if (decoded.role !== "ROLE_ADMIN") {
                setIsValid(false);
                return;
            }

            setIsValid(true);
        } catch (err) {
            localStorage.removeItem("token");
            setIsValid(false);
        }
    }, []);

    if (isValid === null) return <div>Loading...</div>;
    if (!isValid) return <Navigate to="/login" />;

    return children;
}
