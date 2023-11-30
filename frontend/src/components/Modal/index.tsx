import { useEffect } from "react";
import "./style.scss";

const Modal = ({
    message,
    animate,
    visible: errorVisible,
    setAnimate,
    setVisible: setErrorVisible,
    ms,
}: ModalProps) => {
    useEffect(() => {
        if (!errorVisible) return;

        const timeout = setTimeout(() => {
            setAnimate(true);
        }, ms ?? 3000);

        return () => clearTimeout(timeout);
    }, [errorVisible]);

    if (!errorVisible) return null;

    return (
        <div
            className={"error" + (animate ? " on-close" : "")}
            onAnimationEnd={(event) => {
                if (event.animationName !== "fade-out") return;
                setErrorVisible(false);
                setAnimate(false);
            }}
        >
            <span className="error-text">{message}</span>
            <span
                className="material-symbols-outlined"
                onClick={() => {
                    setAnimate(true);
                }}
            >
                close
            </span>
        </div>
    );
};

interface ModalProps {
    message?: string;
    ms?: number;

    visible: boolean;
    setVisible: (state: boolean) => void;

    animate: boolean;
    setAnimate: (state: boolean) => void;
}

export default Modal;
