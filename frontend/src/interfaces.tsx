export interface TagData {
    id: string;
    name: string;
}

export interface ItemData {
    uuid: any;
    id: string;
    name: string;
    tags: TagData[];
}

export interface ListData {
    id: string;
    name: string;
    items: ItemData[];
    tags: TagData[];
    public: boolean;
    archived: boolean;
    ownerId: string;
}

export interface UserData {
    id: string;
    username: string;
    publicLists: ListData[];
}
