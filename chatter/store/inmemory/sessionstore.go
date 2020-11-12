package inmemory

import (
	"github.com/npathai/chatter/model"
	"github.com/npathai/chatter/store"
)

type MemorySessionStore struct {
	sessions []*model.Session
}

func (msStore *MemorySessionStore) Save(session *model.Session) (*model.Session, error) {
	session.PreSave()
	msStore.sessions = append(msStore.sessions, session)
	return session, nil
}

func (msStore *MemorySessionStore) GetSessionById(sessionId string) (*model.Session, error) {
	// TODO Use efficient implementation using map
	for _, s := range msStore.sessions {
		if s.Id == sessionId {
			return s, nil
		}
	}
	return nil, store.NewErrNotFound("Session", sessionId)
}
