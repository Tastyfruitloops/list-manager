import { Link, useNavigate } from "@tanstack/react-router";
import { useState } from "react";
import logo from "../assets/images/icon_alt.svg";
import { Modal } from "../components";
import "./styles/login.scss";

const Signup = () => {
    const navigate = useNavigate();

    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [errorVisible, setErrorVisible] = useState<boolean>(false);
    const [animate, setAnimate] = useState<boolean>(false);

    const [errorMessage, setErrorMessage] = useState<string>(
        "Error occured. Please try again later."
    );

    const login = async () => {
        const response = await fetch(
            `${import.meta.env.VITE_API_URL}/auth/signup`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, password }),
                credentials: "include",
                mode: "cors",
            }
        );

        if (response.status === 201) {
            navigate({ from: "/signup", to: "/" });
        } else {
            setErrorMessage("Invalid username or password.");
            setErrorVisible(true);
        }
    };

    return (
        <>
            <div className="top">
                <Link to="/" className="back">
                    <span className="material-symbols-outlined">
                        chevron_left
                    </span>
                    Back to main page
                </Link>
            </div>
            <div className="login-wrapper">
                <section className="login-form">
                    <img src={logo} />
                    <header>
                        <h2>Register</h2>
                        <span>to continue with Fieldfinder</span>
                    </header>
                    <div className="form">
                        <input
                            type="text"
                            placeholder="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            autoFocus
                        />
                        <input
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            onKeyUp={(e) => {
                                if (e.key === "Enter") login();
                            }}
                        />
                    </div>
                    <div className="actions">
                        <button onClick={login}>Continue</button>
                        <span>
                            Already registered? Log in{" "}
                            <Link to="/login">here</Link>.
                        </span>
                    </div>
                </section>
                <Modal
                    {...{
                        message: errorMessage,
                        animate,
                        visible: errorVisible,
                        setAnimate,
                        setVisible: setErrorVisible,
                    }}
                />
            </div>
        </>
    );
};

export default Signup;
