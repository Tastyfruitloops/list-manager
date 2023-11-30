import { Link, useNavigate, useParams } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { List, Navbar, Sidebar } from "../components";
import { ListData, UserData } from "../interfaces";
import "./styles/home.scss";

const Home = () => {
    const { uuid } = useParams({ from: "/list/$uuid" });
    const navigate = useNavigate();

    const [user, setUser] = useState<UserData>();
    const [lists, setLists] = useState<ListData[]>([]);
    const [selectedList, setSelectedList] = useState<ListData>();

    const [query, setQuery] = useState("");

    useEffect(() => {
        fetch(`${import.meta.env.VITE_API_URL}/users/me`, {
            method: "GET",
            credentials: "include",
            mode: "cors",
        })
            .then((res) => {
                if (res.status === 200) {
                    return res.json();
                }

                throw new Error("Not logged in.");
            })
            .then((user) => setUser(user))
            .catch(() => setUser(undefined));
    }, []);

    useEffect(() => {
        if (!user) {
            return;
        }

        fetch(`${import.meta.env.VITE_API_URL}/lists/`, {
            method: "GET",
            credentials: "include",
            mode: "cors",
        })
            .then((res) => {
                if (res.status === 200) {
                    return res.json();
                }

                throw new Error("Not logged in.");
            })
            .then((res) => setLists(res))
            .catch(() => console.log("Not logged in."));
    }, [user, location]);

    useEffect(() => {
        if (!uuid) {
            return;
        }

        fetch(`${import.meta.env.VITE_API_URL}/lists/${uuid}`, {
            method: "GET",
            credentials: "include",
            mode: "cors",
        })
            .then((res) => {
                if (res.status === 200) {
                    return res.json();
                }

                throw new Error("Not logged in.");
            })
            .then((res) => setSelectedList(res))
            .catch(() => console.log("Not logged in."));
    }, [uuid, location]);

    const onListCreate = async () => {
        const res = await fetch(`${import.meta.env.VITE_API_URL}/lists/`, {
            method: "POST",
            credentials: "include",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: "New list",
            }),
        });

        if (res.status === 201) {
            const data = await res.json();
            setLists([...lists, data]);
        }
    };

    const onListUpdate = async () => {
        const res = await fetch(`${import.meta.env.VITE_API_URL}/lists/`, {
            method: "GET",
            credentials: "include",
            mode: "cors",
        });

        if (res.status === 200) {
            const data = await res.json();
            setLists(data);

            if (uuid) {
                setSelectedList(
                    (data as ListData[]).find((list) => list.id === uuid)
                );
            }
        }
    };

    if (!uuid) {
        if (!user) {
            return (
                <>
                    <Navbar username={""} searchBar={false} />
                    <main>
                        <Sidebar
                            loggedIn={false}
                            atHome={true}
                            lists={lists}
                            onListCreate={onListCreate}
                        />
                        <div className="content home">
                            <h1>Welcome!</h1>
                            <p>
                                You're not logged in. You can{" "}
                                <Link to="/login">log in</Link> or view list by
                                pasting the link below.
                            </p>
                            <div className="link-form">
                                <div className="search-bar">
                                    <span className="material-symbols-outlined">
                                        link
                                    </span>
                                    <input
                                        type="text"
                                        placeholder="Paste link here..."
                                        onKeyDown={(event) => {
                                            if (event.key === "Enter") {
                                                navigate({
                                                    to: "/list/$uuid",
                                                    params: {
                                                        uuid:
                                                            (
                                                                event.target as HTMLInputElement
                                                            ).value
                                                                .split("/")
                                                                .pop() ?? "",
                                                    },
                                                });
                                            }
                                        }}
                                    />
                                </div>
                                <button
                                    onClick={() => {
                                        const uuid =
                                            (
                                                document.querySelector(
                                                    ".link-form .search-bar input"
                                                ) as HTMLInputElement
                                            ).value
                                                .split("/")
                                                .pop() ?? "";

                                        if (!uuid) {
                                            return;
                                        }

                                        navigate({
                                            to: "/list/$uuid",
                                            params: {
                                                uuid: uuid,
                                            },
                                        });
                                    }}
                                >
                                    Open
                                </button>
                            </div>
                        </div>
                    </main>
                </>
            );
        }

        return (
            <>
                <Navbar username={user?.username} searchBar={!!user} />
                <main>
                    <Sidebar
                        loggedIn={!!user}
                        atHome={true}
                        lists={lists}
                        onListCreate={onListCreate}
                    />
                    <div className="content home">
                        <h1>(^^ゞ</h1>
                        <h2>Hi, {user?.username}!</h2>
                        <div className="link-form">
                            <div className="search-bar">
                                <span className="material-symbols-outlined">
                                    link
                                </span>
                                <input
                                    type="text"
                                    placeholder="Paste link here..."
                                    onKeyDown={(event) => {
                                        if (event.key === "Enter") {
                                            navigate({
                                                to: "/list/$uuid",
                                                params: {
                                                    uuid:
                                                        (
                                                            event.target as HTMLInputElement
                                                        ).value
                                                            .split("/")
                                                            .pop() ?? "",
                                                },
                                            });
                                        }
                                    }}
                                />
                            </div>
                            <button
                                onClick={() => {
                                    const uuid =
                                        (
                                            document.querySelector(
                                                ".link-form .search-bar input"
                                            ) as HTMLInputElement
                                        ).value
                                            .split("/")
                                            .pop() ?? "";

                                    if (!uuid) {
                                        return;
                                    }

                                    navigate({
                                        to: "/list/$uuid",
                                        params: {
                                            uuid: uuid,
                                        },
                                    });
                                }}
                            >
                                Open
                            </button>
                        </div>
                    </div>
                </main>
            </>
        );
    }

    if (!selectedList) {
        return (
            <>
                <Navbar username={user?.username} searchBar={!!user} />
                <main>
                    <Sidebar
                        loggedIn={!!user}
                        lists={lists}
                        onListCreate={onListCreate}
                    />
                    <div className="content invalid-list">
                        <h1>(⊙_⊙)？</h1>
                        <p>
                            This list doesn't exist. Maybe try{" "}
                            <Link to="/">going home</Link>?
                        </p>
                    </div>
                </main>
            </>
        );
    }

    return (
        <>
            <Navbar
                username={user?.username}
                searchBar={!!user}
                onSearch={(query) => setQuery(query)}
            />
            <main>
                <Sidebar
                    loggedIn={!!user}
                    lists={lists}
                    selectedList={selectedList}
                    onListCreate={onListCreate}
                />
                <List
                    user={user}
                    list={selectedList}
                    onListUpdate={onListUpdate}
                    query={query}
                />
            </main>
        </>
    );
};

export default Home;
