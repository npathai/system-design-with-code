package store

type ErrNotFound struct {
	resource string
	Id string
}

func NewErrNotFound(resource, id string) *ErrNotFound {
	return &ErrNotFound{
		resource: resource,
		Id: id,
	}
}

func (err *ErrNotFound) Error() string {
	return "resource: " + err.resource + " id: " + err.Id
}