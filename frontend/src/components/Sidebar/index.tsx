import { Link } from "@tanstack/react-router";
import { ListData } from "../../interfaces";
import "./style.scss";

const Sidebar = ({
    loggedIn,
    lists,
    atHome,
    selectedList,
    onListCreate,
}: SidebarProps) => {
    if (!loggedIn) {
        return (
            <nav
                style={{
                    justifyContent: "center",
                    alignItems: "center",
                    gap: "2px",
                }}
            >
                <span
                    className="material-symbols-outlined"
                    style={{
                        fontVariationSettings:
                            "'FILL' 0, 'wght' 300, 'GRAD' 0, 'opsz' 20",
                    }}
                >
                    sentiment_dissatisfied
                </span>
                <p>
                    Nothing to show here. Maybe try{" "}
                    <Link to="/login" className="inline-link">
                        logging in
                    </Link>
                    ?
                </p>
            </nav>
        );
    }

    return (
        <nav>
            <button className="create" onClick={onListCreate}>
                <span className="material-symbols-outlined">add</span>
                Create
            </button>
            <section className="lists">
                <Link className={"list" + (atHome ? " selected" : "")} to="/">
                    <span className="material-symbols-outlined">home</span>
                    Home
                </Link>
                <div className="list header">
                    <span className="material-symbols-outlined">inbox</span>
                    My lists
                </div>
                {lists?.filter((list) => !list.archived).length !== 0 &&
                    lists
                        ?.filter((list) => !list.archived)
                        .map((list) => (
                            <Link
                                className={
                                    "list" +
                                    (list.id === selectedList?.id
                                        ? " selected"
                                        : "")
                                }
                                to="/list/$uuid"
                                params={{ uuid: list.id }}
                                key={list.id}
                            >
                                <span className="material-symbols-outlined">
                                    notes
                                </span>
                                {list.name}
                            </Link>
                        ))}
                {lists?.filter((list) => !list.archived).length === 0 && (
                    <p>Empty section</p>
                )}
                <div className="list header">
                    <span className="material-symbols-outlined">archive</span>
                    Archived lists
                </div>
                {lists?.filter((list) => list.archived).length !== 0 &&
                    lists
                        ?.filter((list) => list.archived)
                        .map((list) => (
                            <Link
                                className={
                                    "list" +
                                    (list.id === selectedList?.id
                                        ? " selected"
                                        : "")
                                }
                                to="/list/$uuid"
                                params={{ uuid: list.id }}
                                key={list.id}
                            >
                                <span className="material-symbols-outlined">
                                    notes
                                </span>
                                {list.name}
                            </Link>
                        ))}
                {lists?.filter((list) => list.archived).length === 0 && (
                    <p>Empty section</p>
                )}
            </section>
        </nav>
    );
};

interface SidebarProps {
    loggedIn?: boolean;
    lists?: ListData[];
    selectedList?: ListData;
    atHome?: boolean;
    onListCreate?: () => void;
}

export default Sidebar;
