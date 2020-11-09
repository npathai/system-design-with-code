package inmemory

import "github.com/npathai/chatter/model"

type MemorySessionStore struct {
	sessions []*model.Session
}

func (store *MemorySessionStore) Save(session *model.Session) (*model.Session, error) {
	// TODO assign Id to session
	store.sessions = append(store.sessions, session)
	return session, nil
}
