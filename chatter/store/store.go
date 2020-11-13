package store

import (
	"github.com/npathai/chatter/model"
	"github.com/npathai/chatter/store/inmemory"
)

type Store interface {
	User() UserStore
	Session() SessionStore
}

type UserStore interface {
	Save(user *model.User) (*model.User, error)
	GetAllUsers() ([]*model.User, error)
	Get(userId string) (*model.User, error)
}

type SessionStore interface {
	Save(session *model.Session) (*model.Session, error)
	GetSessionById(sessionId string) (*model.Session, error)
	GetSessionByToken(token string) (*model.Session, error)
}

type LocalStore struct {
	uStore inmemory.MemoryUserStore
	sStore inmemory.MemorySessionStore
}

func (store *LocalStore) User() UserStore {
	return &store.uStore
}

func (store *LocalStore) Session() SessionStore {
	return &store.sStore
}

func NewStore() *LocalStore {
	store := &LocalStore{}
	store.uStore = inmemory.MemoryUserStore{}
	store.sStore = inmemory.MemorySessionStore{}
	return store
}