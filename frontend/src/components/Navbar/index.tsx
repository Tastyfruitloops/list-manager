import { Link } from "@tanstack/react-router";
import "material-symbols";
import icon from "../../assets/images/icon.svg";
import "./style.scss";

const Navbar = ({ searchBar, username, onSearch }: NavbarProps) => {
    const logout = async () => {
        const response = await fetch(
            `${import.meta.env.VITE_API_URL}/auth/logout`,
            {
                method: "POST",
                credentials: "include",
                mode: "cors",
            }
        );

        if (response.status === 200) {
            window.location.reload();
        }
    };

    const renderSearchBar = () => {
        if (!searchBar) return null;

        return (
            <div className="search-bar">
                <span className="material-symbols-outlined">search</span>
                <input
                    type="text"
                    placeholder="Search"
                    onChange={(event) => {
                        onSearch?.(event.target.value);
                    }}
                />
            </div>
        );
    };

    const renderLogin = () => {
        if (username) {
            return (
                <section>
                    <div className="user">
                        <span className="material-symbols-outlined">
                            account_circle
                        </span>
                        <span className="username">{username}</span>
                    </div>
                    <button onClick={logout}>
                        <span className="material-symbols-outlined logout">
                            logout
                        </span>
                    </button>
                </section>
            );
        }

        return (
            <section>
                <Link className="login" to="/login">
                    <span className="material-symbols-outlined">
                        account_circle
                    </span>
                    <span className="username">Login</span>
                </Link>
            </section>
        );
    };

    return (
        <div className="navbar">
            <Link className="logo" to="/">
                <img src={icon} alt="Logo" />
                <span>Spark</span>
            </Link>
            {renderSearchBar()}
            {renderLogin()}
        </div>
    );
};

interface NavbarProps {
    searchBar?: boolean;
    username?: string;
    onSearch?: (query: string) => void;
}

export default Navbar;
